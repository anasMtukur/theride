package com.anas.theride.location;

import javax.validation.constraints.Null;

import com.anas.theride.driver.Driver;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Payload {
	private String latitude;
	private String longitude;
	@Null
	private String id;
	@Null
	private String geoHashZone;
	@Null
	private Driver driver;
}
