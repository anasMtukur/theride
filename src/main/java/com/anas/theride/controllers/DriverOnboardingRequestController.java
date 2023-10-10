package com.anas.theride.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.naming.NameNotFoundException;
import javax.validation.Valid;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anas.theride.car.Car;
import com.anas.theride.car.CarRepository;
import com.anas.theride.car.CarType;
import com.anas.theride.car.Color;
import com.anas.theride.driver.Driver;
import com.anas.theride.driver.DriverApprovalStatus;
import com.anas.theride.driver.DriverRepository;
import com.anas.theride.driver.Gender;
import com.anas.theride.exceptions.DriverRequestAlreadySentException;
import com.anas.theride.exceptions.EntityNotFoundException;
import com.anas.theride.exceptions.InvalidValueException;
import com.anas.theride.payloads.DriverOnboardingRequestPayload;
import com.anas.theride.payloads.PagedPayload;
import com.anas.theride.role.RoleName;
import com.anas.theride.payloads.DriverOnboardingStatusUpdatePayload;
import com.anas.theride.services.PagingUtil;
import com.anas.theride.user.EndUser;
import com.anas.theride.user.EndUserRepository;
import com.anas.theride.user.EndUserService;

@RestController
@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
@RequestMapping( value="/v1/driver-onboarding" )
public class DriverOnboardingRequestController {
	
	@Autowired
	private DriverRepository driverRepository;

	@Autowired
	private CarRepository carRepository;

	@Autowired 
	EndUserRepository endUserRepository;

	@Autowired
	private EndUserService endUserService;

	@PostMapping(
			  value = "/request", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Driver> request(
			Principal principal,
			@Valid @RequestBody DriverOnboardingRequestPayload payload ) throws Exception {
		
		System.out.println( principal.getName() );
		EndUser requestingUser = endUserRepository.findByUsername( principal.getName() );
		
		//Check if user already has driver role
		Optional<Driver> maybeDriver = driverRepository.findByEndUser( requestingUser );
		if( maybeDriver.isPresent() ) {
			throw new DriverRequestAlreadySentException( maybeDriver.get().getApprovalStatus() );
		}

		if( !EnumUtils.isValidEnum(Gender.class, payload.getGender()) ){
			throw new InvalidValueException("Gender", payload.getGender());
		}

		if( !EnumUtils.isValidEnum(CarType.class, payload.getCarType()) ){
			throw new InvalidValueException("CarType", payload.getCarType());
		}

		if( !EnumUtils.isValidEnum(Color.class, payload.getCarColor()) ){
			throw new InvalidValueException("Color", payload.getCarColor());
		}

		String[] names = requestingUser.getFullName().split(" ");
		Driver entity = new Driver(); 
		System.out.println( payload.getGender() + " === " + Gender.valueOf( payload.getGender() ) );
		entity.setActiveCity( payload.getCity() );
		entity.setEndUser( requestingUser );
		entity.setFirstName( names[0] );
		entity.setLastName( names[1] );
		entity.setGender( Gender.valueOf( payload.getGender() ) );
		entity.setApprovalStatus( DriverApprovalStatus.PENDING );
		entity.setLicenseNumber( payload.getLicenseNumber() );

		driverRepository.saveAndFlush( entity );

		if( entity.getId() != null ) {
			String brandModel = payload.getCarBrand() + " " + payload.getCarModel();
			Car carEntity = new Car( 
					entity, 
					Color.valueOf( payload.getCarColor() ), 
					brandModel, 
					payload.getCarPlateNo(), 
					CarType.valueOf( payload.getCarType() ) 
				);
			carRepository.saveAndFlush( carEntity );
		}
		
		return ResponseEntity.ok( entity );
	}

	@GetMapping( produces = MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity<PagedPayload<Driver>> findWaiting(
			Principal principal,
			@RequestParam(name = "page", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(name = "search", required = false, defaultValue = StringUtils.EMPTY) String searchCriteria,
            @RequestParam(name = "searchtext", required = false, defaultValue = "") String searchText,
            @RequestParam(name = "pagesize", required = false, defaultValue = "1000") int pageSize,
            @RequestParam(name = "sortby", required = false, defaultValue = "date") String sortBy,
            @RequestParam(name = "sortdirection", required = false, defaultValue = "desc") String sortDirection,
            @RequestParam(name = "createdBy", required = false, defaultValue = "") String createdBy) throws Exception {
		
		PagedPayload<Driver> response = new PagedPayload<Driver>();
		PageRequest pageRequest = PagingUtil.getPageRequestObject( pageNumber, pageSize, sortBy, sortDirection );
		Page<Driver> pageData = driverRepository.findByApprovalStatus( DriverApprovalStatus.PENDING, pageRequest );
		
		/* List<Payload> payloads = pageData.getContent()
				.stream()
				.map((p) -> service.fromEndUser(p))
				.collect( Collectors.toList() ); */
        
		response.setTotalPages( pageData.getTotalPages() );
		response.setTotalElements( pageData.getTotalElements() );
		response.setItems( pageData.getContent() );
		
		return ResponseEntity.ok( response );
	}

	@GetMapping( value="/user", produces = MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity<Driver> findWaitingByPrincipal(
			Principal principal) throws Exception {
		EndUser requestingUser = endUserRepository.findByUsername( principal.getName() );
		Driver entity = driverRepository.findByEndUser( requestingUser )
											.orElseThrow(() -> new EntityNotFoundException( "Driver with the given ID does not exist." ));
				
		return ResponseEntity.ok( entity );
	}

	@PostMapping(
			  value = "/update-status", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Driver> updateStatus(
			Principal principal,
			@Valid @RequestBody DriverOnboardingStatusUpdatePayload payload ) throws Exception {
		
		EndUser requestingUser = endUserRepository.findByUsername( principal.getName() );

		if( !EnumUtils.isValidEnum(DriverApprovalStatus.class, payload.getStatus()) ){
			throw new InvalidValueException("DriverApprovalStatus", payload.getStatus());
		}

		Driver entity = driverRepository.findById( UUID.fromString( payload.getDriverId() ) )
													.orElseThrow(() -> new EntityNotFoundException( "Driver with the given ID does not exist." ));

		entity.setApprovalStatus( DriverApprovalStatus.valueOf( payload.getStatus() ) );
		
		driverRepository.saveAndFlush( entity );

		if( entity.getApprovalStatus().equals( DriverApprovalStatus.APPROVED ) ){
			EndUser driverEndUser = entity.getEndUser();
			List<RoleName> roles = new ArrayList<>( Arrays.asList(
				RoleName.ROLE_DRIVER
			));

			endUserService.addEndUserRoles(driverEndUser, roles);
		}

		return ResponseEntity.ok( entity );
	}
}
