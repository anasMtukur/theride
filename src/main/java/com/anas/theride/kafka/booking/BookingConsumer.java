package com.anas.theride.kafka.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.anas.theride.kafka.location.LocationUpdateData;
import com.anas.theride.location.LocationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BookingConsumer {
	private static final String TOPIC = "theride-booking-notification";
	private static final String GROUP_ID = "TRPBNG";

	@KafkaListener( topics = TOPIC, groupId = GROUP_ID )
    public void consume(String message) {
		ObjectMapper objectMapper = new ObjectMapper();
        try {
            //What are we consuming with booking?
            //JsonProcessingException 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
