package com.anas.theride.booking;

import javax.validation.constraints.Null;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewBookingPayload {
	private double pickupLatitude;
	private double pickupLongitude;
	private double dropOffLatitude;
	private double dropOffLongitude;
	private String bookingType;

}
