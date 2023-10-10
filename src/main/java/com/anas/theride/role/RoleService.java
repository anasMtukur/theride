package com.anas.theride.role;

import org.springframework.stereotype.Component;

@Component
public class RoleService {
	public Payload fromModel( Role model ) {
		Payload payload = new Payload();
		
		return payload;
	}
	
	public Role fromPayload( Payload payload ) {
		Role model = new Role();
		
		return model;
	}
}
