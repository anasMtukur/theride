package com.anas.theride.kafka.booking;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class BookingTopicConfig {
	private static final String TOPIC = "theride-booking-notification";
	
    @Bean
    public NewTopic createLocationUpdateTopic(){
        return TopicBuilder.name( TOPIC )
                .build();
    }
}
