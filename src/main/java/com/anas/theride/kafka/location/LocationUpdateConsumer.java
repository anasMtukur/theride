package com.anas.theride.kafka.location;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.anas.theride.location.LocationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class LocationUpdateConsumer {
	private static final String TOPIC = "theride-location-update";
	private static final String GROUP_ID = "TRDLUG";

	@Autowired
	LocationService service;

    @KafkaListener( topics = TOPIC, groupId = GROUP_ID )
    public void consume(String message) {
		ObjectMapper objectMapper = new ObjectMapper();
        try {
            LocationUpdateData lupData = objectMapper.readValue(message, LocationUpdateData.class);
			service.saveLocation(lupData);
            
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
