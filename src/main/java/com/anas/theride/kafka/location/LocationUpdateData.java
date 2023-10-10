package com.anas.theride.kafka.location;

import java.io.Serializable;

import javax.validation.constraints.Null;

import lombok.Data;

@Data
public class LocationUpdateData implements Serializable{
	private String latitude;
	private String longitude;
	@Null
	private String userId;
}
