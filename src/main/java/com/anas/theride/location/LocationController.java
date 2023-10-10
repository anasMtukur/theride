package com.anas.theride.location;

import java.security.Principal;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anas.theride.driver.Driver;
import com.anas.theride.driver.DriverRepository;
import com.anas.theride.exceptions.DriverRequestAlreadySentException;
import com.anas.theride.exceptions.EntityNotFoundException;
import com.anas.theride.lastknownlocation.LastKnownLocation;
import com.anas.theride.lastknownlocation.LastKnownLocationRepository;
import com.anas.theride.user.EndUser;
import com.anas.theride.user.EndUserRepository;

@RestController
@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
@RequestMapping( value="/v1/location" )
public class LocationController {

	@Autowired
	LocationRepository repository;

	@Autowired 
	DriverRepository driverRepository;

	@Autowired 
	EndUserRepository endUserRepository;

	@Autowired
	LocationService service;

	@Autowired
	LastKnownLocationRepository lastKnownLocationRepository;

	@PostMapping
	public ResponseEntity<String> add(
			Principal principal,
			@Valid @RequestBody Payload payload ) throws Exception {

		EndUser requestingUser = endUserRepository.findByUsername( principal.getName() );
		
		//Check if user already has driver role
		Driver driver = driverRepository.findByEndUser( requestingUser )
														.orElseThrow(() -> new EntityNotFoundException( "Driver with the given ID does not exist." ));
		
		Location entity = service.fromPayload(payload, driver );
		repository.save( entity );
		
		if( entity.getId() != null ) {
			//update last known location
			LastKnownLocation lkl = lastKnownLocationRepository.findByDriver(driver);
			if( lkl == null ){
				lkl = new LastKnownLocation(driver, entity, Double.MAX_VALUE);
			}
			lkl.setLocation(entity);

			lastKnownLocationRepository.saveAndFlush( lkl );
		}
		
		return ResponseEntity.ok( String.valueOf( entity.getId() ) );
	}
	
}
