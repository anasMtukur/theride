package com.anas.theride.booking;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.anas.theride.driver.Driver;
import com.anas.theride.passenger.Passenger;

@Repository
public interface BookingRepository  extends JpaRepository<Booking, UUID>{

	List<Booking> findByGeoHashZoneAndBookingStatus(String geoHash, BookingStatus bookingStatus);

	@Query("SELECT e FROM Booking e WHERE e.geoHashZone = :geoHashZone AND e.bookingStatus IN ('ASSIGNING_DRIVER', 'ACCEPTED')")
    List<Booking> findByGeoHashZoneAndBookingStatus(@Param("geoHashZone") String geoHashZone);

	List<Booking> findByPassenger( Passenger passenger );

	List<Booking> findByDriver( Driver driver );
	
}
