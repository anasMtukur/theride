package com.anas.theride.booking;

import org.springframework.stereotype.Component;

@Component
public class BookingService {

	public NewBookingPayload fromEntity( Booking entity ) {
		NewBookingPayload payload = new NewBookingPayload();
		
		return payload;
	}

	public Booking fromPayload( NewBookingPayload payload ) {
		Booking booking = new Booking();
		
		return booking;
	}

	
	
}
