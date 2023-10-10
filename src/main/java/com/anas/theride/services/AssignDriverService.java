package com.anas.theride.services;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anas.theride.booking.Booking;
import com.anas.theride.booking.BookingPoint;
import com.anas.theride.booking.BookingPointType;
import com.anas.theride.booking.BookingRepository;
import com.anas.theride.booking.BookingStatus;
import com.anas.theride.driver.Driver;
import com.anas.theride.lastknownlocation.LastKnownLocation;
import com.anas.theride.lastknownlocation.LastKnownLocationRepository;
import com.anas.theride.services.ats.DistanceCalculatorService;

@Service
public class AssignDriverService {
	@Autowired
	DistanceCalculatorService distanceCalculator;

	@Autowired
	BookingRepository bookingRepository;

	@Autowired
	LastKnownLocationRepository lastKnownLocationRepository;

	@Transactional
	public boolean assignClosestDriverToBooking( Booking booking ){
		BookingPoint pickup = booking.getPoints().stream()
										.filter( point -> point.getPointType().equals( BookingPointType.PICKUP ))
										.collect(Collectors.toList()).get(0);

		//Find drivers within the booking hashZone 
		List<LastKnownLocation> drivers = lastKnownLocationRepository.findByLocationGeoHashZone( booking.getGeoHashZone() );

		drivers = drivers.stream().map((d) -> this.calculateRoadDistance(d, pickup)).collect(Collectors.toList());
		
		//Sort by distance
		Driver selected = findClosestDriver( drivers );

		booking.setDriver( selected );
		booking.setBookingStatus( BookingStatus.ACCEPTED );
		bookingRepository.saveAndFlush( booking );

		return booking.getDriver() != null && booking.getBookingStatus().equals( BookingStatus.ACCEPTED );
	}

	public LastKnownLocation calculateRoadDistance( LastKnownLocation item, BookingPoint pickup ){
		double distanceToPickup = distanceCalculator.calculateRoadDistance( 
				Double.parseDouble(pickup.getLatitude()), 
				Double.parseDouble(pickup.getLongitude()), 
				Double.parseDouble(item.getLocation().getLatitude()), 
				Double.parseDouble(item.getLocation().getLongitude()));

			
		item.setDistance(distanceToPickup);

		return item;
	}

	private Driver findClosestDriver( List<LastKnownLocation> drivers ){
		
		List<LastKnownLocation> sorted = drivers.stream()
			.sorted(Comparator.comparing(LastKnownLocation::getDistance))
			.collect(Collectors.toList());

		LastKnownLocation closest = sorted.get(0);

		return closest.getDriver();
	}
}
