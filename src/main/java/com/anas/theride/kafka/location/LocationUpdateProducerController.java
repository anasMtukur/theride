package com.anas.theride.kafka.location;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.anas.theride.user.EndUser;
import com.anas.theride.user.EndUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/v1/kafka")
public class LocationUpdateProducerController {

	private LocationUpdateProducer kafkaProducer;

	@Autowired 
	EndUserRepository endUserRepository;

    public LocationUpdateProducerController(LocationUpdateProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

	@PostMapping("/publish")
    public ResponseEntity<String> publish(Principal principal,
			@Valid @RequestBody LocationUpdateData payload ) throws Exception{

		EndUser requestingUser = endUserRepository.findByUsername( principal.getName() );
		ObjectMapper objectMapper = new ObjectMapper();
		payload.setUserId( String.valueOf(requestingUser.getId()) );

        // Serialize LocationUpdateData to JSON string
        String message = objectMapper.writeValueAsString(payload);
        kafkaProducer.sendMessage(message);
        return ResponseEntity.ok("Message sent to kafka topic");
    }
	
}
