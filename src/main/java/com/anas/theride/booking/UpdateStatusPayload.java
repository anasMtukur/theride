package com.anas.theride.booking;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateStatusPayload {
	private String id;
	private String status;
}
