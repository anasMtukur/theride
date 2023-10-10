package com.anas.theride.booking;

import java.util.List;

import javax.validation.constraints.Null;

import com.anas.theride.lastknownlocation.LastKnownLocation;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingInfoPayload {
	private Booking booking;
	
	@Null
	private List<LastKnownLocation> drivers;
}
