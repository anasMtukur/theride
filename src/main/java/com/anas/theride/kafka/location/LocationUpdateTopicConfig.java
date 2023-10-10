package com.anas.theride.kafka.location;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class LocationUpdateTopicConfig {
	private static final String TOPIC = "theride-location-update";
	
    @Bean
    public NewTopic createLocationUpdateTopic(){
        return TopicBuilder.name( TOPIC )
                .build();
    }
}