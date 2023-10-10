package com.anas.theride.payloads;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DriverOnboardingRequestPayload {
	private String gender;
	private String city;
	private String licenseNumber;
	private String carColor;
	private String carBrand;
	private String carModel;
	private String carPlateNo;
	private String carType;
}
