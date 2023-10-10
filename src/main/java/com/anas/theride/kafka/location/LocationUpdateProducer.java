package com.anas.theride.kafka.location;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class LocationUpdateProducer {
	private static final String TOPIC    = "theride-location-update";
	private static final String GROUP_ID = "TRDLUG";
	private static final Logger LOGGER   = LoggerFactory.getLogger(LocationUpdateProducer.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

	public void sendMessage(String message){
        LOGGER.info(String.format("Message sent -> %s", message));
        kafkaTemplate.send( TOPIC, message);
    }
}
