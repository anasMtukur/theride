package com.anas.theride.location;

import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import ch.hsr.geohash.GeoHash;

import com.anas.theride.driver.Driver;
import com.anas.theride.driver.DriverRepository;
import com.anas.theride.exceptions.EntityNotFoundException;
import com.anas.theride.kafka.location.LocationUpdateData;
import com.anas.theride.lastknownlocation.LastKnownLocation;
import com.anas.theride.lastknownlocation.LastKnownLocationRepository;
import com.anas.theride.user.EndUser;
import com.anas.theride.user.EndUserRepository;

@Component
@Service
public class LocationService {
	private static double MAX_RADIUS = 10.0;
	private static int PRECISION     = 12;

	@Autowired
	LocationRepository repository;

	@Autowired 
	DriverRepository driverRepository;

	@Autowired 
	EndUserRepository endUserRepository;

	@Autowired
	LastKnownLocationRepository lastKnownLocationRepository;
	
	@PostConstruct
    private void postConstruct() {
        System.out.println("Location Service bean created!");
    }

	public Payload fromEntity( Location entity ){
		Payload payload = new Payload();

		payload.setId( String.valueOf( entity.getId() ) );
		payload.setDriver( entity.getDriver() );
		payload.setLatitude( entity.getLatitude() );
		payload.setLongitude( entity.getLongitude() );
		payload.setGeoHashZone( entity.getGeoHashZone() );

		return payload;
	}

	public Location fromPayload( Payload payload, Driver driver ){
		Location entity = new Location();

		double latitude  = Double.parseDouble( payload.getLatitude() );
		double longitude = Double.parseDouble( payload.getLongitude() );

		String geoHashZone = generateGeoHash(latitude, longitude);
		if( payload.getId() != null ) {
			entity.setId( UUID.fromString( payload.getId() ) );
		}
		entity.setDriver( driver );
		entity.setLatitude( payload.getLatitude() );
		entity.setLongitude( payload.getLongitude() );
		entity.setGeoHashZone( geoHashZone );

		return entity;
	}

	public String generateGeoHash( double latitude, double longitude ){

		return GeoHash.withCharacterPrecision(latitude, longitude, PRECISION).toBase32();
	}

	public Location findLastKnownDriverLocation( Driver driver ){
		Location lastKnownLocation = null;

		Pageable pageable = PageRequest.of(0, 1); 
		Page<Location> page = repository.findLatestLocationForDriver( driver, pageable );
		if( page.getNumberOfElements() > 0 ){
			lastKnownLocation = page.getContent().get( 0 );
		}

		return lastKnownLocation;
	}

	public String saveLocation( LocationUpdateData data ){
		EndUser requestingUser = endUserRepository.findById( UUID.fromString( data.getUserId() ) )
													.orElseThrow( () -> new EntityNotFoundException( "Driver with the given ID does not exist." ) );
		
		//Check if user already has driver role
		Driver driver = driverRepository.findByEndUser( requestingUser )
											.orElseThrow(() -> new EntityNotFoundException( "Driver with the given ID does not exist." ));
		
		double latitude  = Double.parseDouble( data.getLatitude() );
		double longitude = Double.parseDouble( data.getLongitude() );
											
		Location entity = new Location();
		String geoHashZone = generateGeoHash(latitude, longitude);
		entity.setDriver( driver );
		entity.setLatitude( data.getLatitude() );
		entity.setLongitude( data.getLongitude() );
		entity.setGeoHashZone( geoHashZone );
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
		
		return String.valueOf( entity.getId() );
	}
	
}
