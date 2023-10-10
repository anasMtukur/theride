package com.anas.theride.booking;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FindByLocationPayload {
	private double latitude;
	private double longitude;
}
