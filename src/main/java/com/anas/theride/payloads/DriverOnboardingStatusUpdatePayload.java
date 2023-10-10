package com.anas.theride.payloads;

import lombok.Data;

@Data
public class DriverOnboardingStatusUpdatePayload {
	String driverId;
	String status;
}
