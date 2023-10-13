package com.anas.theride.booking;

import com.anas.theride.booking.*;
import com.anas.theride.driver.Driver;
import com.anas.theride.driver.DriverRepository;
import com.anas.theride.driver.Gender;
import com.anas.theride.exceptions.EntityNotFoundException;
import com.anas.theride.lastknownlocation.LastKnownLocationRepository;
import com.anas.theride.location.LocationService;
import com.anas.theride.passenger.Passenger;
import com.anas.theride.passenger.PassengerRepository;
import com.anas.theride.payloads.PagedPayload;
import com.anas.theride.services.AssignDriverService;
import com.anas.theride.services.ats.DistanceCalculatorService;
import com.anas.theride.user.EndUser;
import com.anas.theride.user.EndUserRepository;
import com.anas.theride.user.EndUserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class BookingControllerTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BookingService bookingService;

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private EndUserRepository endUserRepository;

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private BookingPointRepository bookingPointRepository;

    @Mock
    private LocationService locationService;

    @Mock
    private EndUserService endUserService;

    @Mock
    private AssignDriverService assignDriverService;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private DistanceCalculatorService distanceCalculator;

    @Mock
    private LastKnownLocationRepository lastKnownLocationRepository;

    @InjectMocks
    private BookingController controller;

	@BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testFind() throws Exception {
        int pageNumber = 0;
        int pageSize = 10;
        String sortBy = "date";
        String sortDirection = "desc";

        Page<Booking> page = mock(Page.class);
        when(bookingRepository.findAll(any(PageRequest.class))).thenReturn(page);
        when(page.getTotalPages()).thenReturn(1);
        when(page.getTotalElements()).thenReturn(1L);
        when(page.getContent()).thenReturn(Collections.singletonList(new Booking()));

        ResponseEntity<PagedPayload<Booking>> response = controller.find(mock(Principal.class),
                pageNumber, "", "", pageSize, sortBy, sortDirection, "");

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalPages());
        assertEquals(1, response.getBody().getTotalElements());
        assertEquals(1, response.getBody().getItems().size());
    }

    @Test
    void testFindById() throws Exception {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("username");

        EndUser requestingUser = new EndUser();
        when(endUserRepository.findByUsername(eq("username"))).thenReturn(requestingUser);

        Booking booking = new Booking();
        booking.setId(UUID.randomUUID());
		booking.setBookingStatus(BookingStatus.ACCEPTED);
        when(bookingRepository.findById(any(UUID.class))).thenReturn(Optional.of(booking));

        ResponseEntity<BookingInfoPayload> response = controller.findById(principal, "passenger", String.valueOf(booking.getId()));

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(booking.getId(), response.getBody().getBooking().getId());
    }

    @Test
    void testAdd() throws Exception {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("username");

        EndUser requestingUser = new EndUser();
		requestingUser.setId( UUID.randomUUID() );
		requestingUser.setFullName("Test User");

		Passenger passenger = new Passenger();
		passenger.setEndUser( requestingUser );
		passenger.setFirstName( "Test" );
		passenger.setLastName( "User" );
		passenger.setGender( Gender.MALE );

		Passenger savedPassenger = passenger;
		savedPassenger.setId( UUID.randomUUID() );

		NewBookingPayload payload =  new NewBookingPayload();
		payload.setBookingType( "GO" );

        when(endUserRepository.findByUsername(eq("username"))).thenReturn(requestingUser);
		when(passengerRepository.saveAndFlush(any(Passenger.class))).thenReturn(savedPassenger);

        ResponseEntity<Booking> response = controller.add(principal, payload);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    void testFindByGeoHash() throws Exception {
        ResponseEntity<List<Booking>> response = controller.findByGeoHash(mock(Principal.class), mock(FindByLocationPayload.class));

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    void testUpdateBookingStatus() throws Exception {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("username");

        Driver driver = new Driver();
		driver.setId( UUID.randomUUID() );
        when(driverRepository.findByEndUser(any())).thenReturn(Optional.of(driver));

        Booking booking = new Booking();
        booking.setId(UUID.randomUUID());
		booking.setDriver(driver);
        when(bookingRepository.findById(any(UUID.class))).thenReturn(Optional.of(booking));

		UpdateStatusPayload payload = new UpdateStatusPayload( String.valueOf( UUID.randomUUID() ), "IN_RIDE"); 

        ResponseEntity<Booking> response = controller.updateBookingStatus(principal, payload);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    void testFindHistory() throws Exception {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("username");

		Driver d = new Driver(null, null, null, null, null, null, null, null, null, null, null);

        Optional<Driver> maybeDriver = Optional.of(d);
        when(driverRepository.findByEndUser(any())).thenReturn(maybeDriver);
		
		Booking b1 = new Booking(null, d, null, BookingType.GO, BookingStatus.COMPLETED, null);
		b1.setId( UUID.randomUUID() );
		b1.setCreatedAt( new Date() );

		Booking b2 = new Booking(null, d, null, BookingType.GO, BookingStatus.COMPLETED, null);
		b2.setId( UUID.randomUUID() );
		b2.setCreatedAt( new Date() );


        List<Booking> bookings = Arrays.asList(b1, b2);
        when(bookingRepository.findByDriver(any())).thenReturn(bookings);

        ResponseEntity<List<Booking>> response = controller.findHistory(principal, "driver");

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }
}
