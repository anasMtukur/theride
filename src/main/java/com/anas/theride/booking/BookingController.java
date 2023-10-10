package com.anas.theride.booking;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anas.theride.driver.Driver;
import com.anas.theride.driver.DriverRepository;
import com.anas.theride.driver.Gender;
import com.anas.theride.exceptions.DriverRequestAlreadySentException;
import com.anas.theride.exceptions.EntityNotFoundException;
import com.anas.theride.lastknownlocation.LastKnownLocation;
import com.anas.theride.lastknownlocation.LastKnownLocationRepository;
import com.anas.theride.location.LocationService;
import com.anas.theride.passenger.Passenger;
import com.anas.theride.passenger.PassengerRepository;
import com.anas.theride.payloads.PagedPayload;
import com.anas.theride.role.RoleName;
import com.anas.theride.services.AssignDriverService;
import com.anas.theride.services.PagingUtil;
import com.anas.theride.services.ats.DistanceCalculatorService;
import com.anas.theride.user.EndUser;
import com.anas.theride.user.EndUserRepository;
import com.anas.theride.user.EndUserService;

@RestController
@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
@RequestMapping( value="/v1/booking" )
public class BookingController {
	private static final String TOPIC = "theride-booking-notification";

	@Autowired
	private BookingRepository repository;

	@Autowired
	private BookingService service;

	@Autowired
	private DriverRepository driverRepository;

	@Autowired 
	private EndUserRepository endUserRepository;

	@Autowired 
	private PassengerRepository passengerRepository;

	@Autowired
	private BookingPointRepository bookingPointRepository;

	@Autowired
	LocationService locationService;

	@Autowired
	EndUserService endUserService;

	@Autowired
	AssignDriverService assignDriverService;

	@Autowired
 	private SimpMessagingTemplate messagingTemplate;

	@Autowired
	DistanceCalculatorService distanceCalculator;

	@Autowired
	LastKnownLocationRepository lastKnownLocationRepository;

	@GetMapping( produces = MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity<PagedPayload<Booking>> find(
			Principal principal,
			@RequestParam(name = "page", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(name = "search", required = false, defaultValue = StringUtils.EMPTY) String searchCriteria,
            @RequestParam(name = "searchtext", required = false, defaultValue = "") String searchText,
            @RequestParam(name = "pagesize", required = false, defaultValue = "1000") int pageSize,
            @RequestParam(name = "sortby", required = false, defaultValue = "date") String sortBy,
            @RequestParam(name = "sortdirection", required = false, defaultValue = "desc") String sortDirection,
            @RequestParam(name = "createdBy", required = false, defaultValue = "") String createdBy) throws Exception {
		
		PagedPayload<Booking> response = new PagedPayload<Booking>();
		PageRequest pageRequest = PagingUtil.getPageRequestObject( pageNumber, pageSize, sortBy, sortDirection );
		Page<Booking> pageData = repository.findAll(pageRequest);
		
		response.setTotalPages( pageData.getTotalPages() );
		response.setTotalElements( pageData.getTotalElements() );
		response.setItems(pageData.getContent());
		
		return ResponseEntity.ok( response );
	}

	@GetMapping( value="{mode}/{id}", produces = MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity<BookingInfoPayload> findById(
			Principal principal,
			@PathVariable( name = "mode", required = true ) String mode,
			@PathVariable( name = "id", required = true ) String id) throws Exception {
		EndUser requestingUser = endUserRepository.findByUsername( principal.getName() );

		Booking booking = repository.findById( UUID.fromString(id) )
			.orElseThrow( () -> new EntityNotFoundException ("Booking with the given id does not exist"));

		List<LastKnownLocation> drivers = null;

		if( mode.equals( "passenger" ) && booking.getBookingStatus().equals( BookingStatus.ASSIGNING_DRIVER ) ){
			BookingPoint pickup = booking.getPoints().stream()
				.filter( point -> point.getPointType().equals( BookingPointType.PICKUP ))
				.collect(Collectors.toList()).get(0);
			
			System.out.println( booking.getGeoHashZone() );

			drivers = lastKnownLocationRepository.findByLocationGeoHashZone( booking.getGeoHashZone() );
			System.out.println( drivers.size() );
			drivers = drivers.stream()
								.map((d) -> assignDriverService.calculateRoadDistance(d, pickup))
								.sorted(Comparator.comparing(LastKnownLocation::getDistance))
								.collect(Collectors.toList());
		}
					
		return ResponseEntity.ok( new BookingInfoPayload( booking, drivers ) );
	}

	@PostMapping
	//@SendTo("/" +TOPIC+ "/incoming-bookings")
	@SendTo("/incoming-bookings")
	public Booking add(
			Principal principal,
			@Valid @RequestBody NewBookingPayload payload ) throws Exception {
		EndUser requestingUser = endUserRepository.findByUsername( principal.getName() );
		Optional<Passenger> optPass = passengerRepository.findByEndUser( requestingUser );
		
		Passenger passenger = optPass.orElseGet(() -> new Passenger());
		if( !optPass.isPresent() ){
			String[] names = requestingUser.getFullName().split(" ");

			passenger.setEndUser( requestingUser );
			passenger.setFirstName( names[0] );
			passenger.setLastName( names[1] );
			passenger.setGender( Gender.MALE );
			passengerRepository.saveAndFlush( passenger );

			if( passenger.getId() == null ){
				throw new RuntimeException("Failed to start a passenger account");
			}

		}

		String pickupGeoHash = locationService.generateGeoHash( payload.getPickupLatitude(), payload.getPickupLongitude() );
		Booking entity = new Booking();
		entity.setPassenger( passenger );
		entity.setBookingType( BookingType.valueOf( payload.getBookingType() ) );
		entity.setBookingStatus( BookingStatus.ASSIGNING_DRIVER );
		entity.setGeoHashZone(pickupGeoHash);
		
		repository.save( entity );
		
		if( entity.getId() != null ) {
			List<BookingPoint> points = new ArrayList<BookingPoint>();
			BookingPoint pickup  = new BookingPoint();
			
			pickup.setBooking(entity);
			pickup.setPointType( BookingPointType.PICKUP );
			pickup.setLatitude( String.valueOf(payload.getPickupLatitude()) );
			pickup.setLongitude( String.valueOf(payload.getPickupLongitude()) );
			pickup.setGeoHashZone(pickupGeoHash);

			BookingPoint dropoff = new BookingPoint();
			String dropOffGeoHash = locationService.generateGeoHash( payload.getDropOffLatitude(), payload.getDropOffLongitude() );

			dropoff.setBooking(entity);
			dropoff.setPointType( BookingPointType.DROPOFF );
			dropoff.setLatitude( String.valueOf(payload.getDropOffLatitude()) );
			dropoff.setLongitude( String.valueOf(payload.getDropOffLongitude()) );
			dropoff.setGeoHashZone(dropOffGeoHash);

			points.add(pickup);
			points.add(dropoff);

			bookingPointRepository.saveAllAndFlush( points );

			entity.setPoints( points );
		}

		//assignDriverUsingService
		//assignDriverService.assignClosestDriverToBooking( entity );

		messagingTemplate.convertAndSend("/incoming-bookings", entity);

		return entity;
	}

	@PostMapping( value="/zone", produces = MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity<List<Booking>> findByGeoHash(
			Principal principal,
			@Valid @RequestBody FindByLocationPayload payload) throws Exception {
		
		String geoHash = locationService.generateGeoHash( payload.getLatitude(), payload.getLongitude() );
		System.out.println( geoHash );
		List<Booking> response = repository.findByGeoHashZoneAndBookingStatus( geoHash );
		
		return ResponseEntity.ok( response );
	}

	@PutMapping( value="/status", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity<Booking> updateBookingStatus(
			Principal principal,
			@Valid @RequestBody UpdateStatusPayload payload ) throws Exception {
		EndUser requestingUser = endUserRepository.findByUsername( principal.getName() );
		Driver driver = driverRepository.findByEndUser( requestingUser ).orElseThrow(() -> new Exception( "User is not a driver!" ));

		String id = payload.getId();
		String status = payload.getStatus();
		BookingStatus bookingStatus = BookingStatus.valueOf( status );

		Booking booking = repository.findById( UUID.fromString(id) ).orElseThrow( () -> new EntityNotFoundException ("Booking with the given id does not exist"));
		
		if( booking.getDriver() == null && bookingStatus.equals( BookingStatus.ACCEPTED ) ){
			booking.setDriver(driver);
		}

		System.out.println( driver.getId() + "" + booking.getDriver().getId() );
		if( !driver.getId().equals( booking.getDriver().getId() ) ){
			throw new RuntimeException( "Driver is not assigned to ride!" );
		}

		booking.setBookingStatus( bookingStatus );
		repository.saveAndFlush( booking );

		return ResponseEntity.ok( booking );
	}

	@GetMapping( value="{mode}/history", produces = MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity<List<Booking>> findHistory(
			Principal principal,
			@PathVariable( name = "mode", required = true ) String mode ) throws Exception {
		EndUser requestingUser = endUserRepository.findByUsername( principal.getName() );

		Optional<Driver> maybeDriver = driverRepository.findByEndUser( requestingUser );
		Optional<Passenger> maybePassenger = passengerRepository.findByEndUser( requestingUser );


		List<Booking> bookings = new ArrayList<Booking>();

		if( mode.equals( "driver" ) && maybeDriver.isPresent() ){
			bookings = repository.findByDriver( maybeDriver.orElseGet(null) );
		}

		if( mode.equals( "passenger" ) && maybePassenger.isPresent() ){
			bookings = repository.findByPassenger( maybePassenger.orElseGet(null) );
		}

		bookings = bookings.stream()
					.sorted(Comparator.comparing(Booking::getCreatedAt))
					.collect(Collectors.toList());
					
		return ResponseEntity.ok( bookings );
	}
}
