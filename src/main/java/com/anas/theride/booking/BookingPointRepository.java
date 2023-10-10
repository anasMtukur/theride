package com.anas.theride.booking;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingPointRepository  extends JpaRepository<BookingPoint, UUID>{
	
}
