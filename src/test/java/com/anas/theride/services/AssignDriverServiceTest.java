package com.anas.theride.services;

import com.anas.theride.booking.Booking;
import com.anas.theride.booking.BookingPoint;
import com.anas.theride.booking.BookingPointType;
import com.anas.theride.booking.BookingRepository;
import com.anas.theride.booking.BookingStatus;
import com.anas.theride.driver.Driver;
import com.anas.theride.lastknownlocation.LastKnownLocation;
import com.anas.theride.lastknownlocation.LastKnownLocationRepository;
import com.anas.theride.location.Location;
import com.anas.theride.services.ats.DistanceCalculatorService;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AssignDriverServiceTest {

    @Mock
    private DistanceCalculatorService distanceCalculator;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private LastKnownLocationRepository lastKnownLocationRepository;

    @InjectMocks
    private AssignDriverService assignDriverService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAssignClosestDriverToBooking() {
        // Mock booking
        Booking booking = new Booking();
        BookingPoint pickupPoint = new BookingPoint();
        pickupPoint.setPointType(BookingPointType.PICKUP);
        pickupPoint.setLatitude("40.7128");
        pickupPoint.setLongitude("-74.0060");
        booking.setPoints(Arrays.asList(pickupPoint));
        booking.setGeoHashZone("someGeoHash");

        // Mock drivers
        LastKnownLocation driver1 = createDriver("42a511d3-447f-11e8-842f-0ed5f89f718b", "40.7130", "-74.0065");
        LastKnownLocation driver2 = createDriver("15f974d1-acb6-4d33-847b-9aca0e0decce", "40.7115", "-74.0055");

        when(lastKnownLocationRepository.findByLocationGeoHashZone(anyString()))
                .thenReturn(Arrays.asList(driver1, driver2));

        // Mock distance calculations
        when(distanceCalculator.calculateRoadDistance(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(100.0); // Distance is irrelevant for this test

        // Invoke the method
        boolean result = assignDriverService.assignClosestDriverToBooking(booking);

        // Verify that bookingRepository.saveAndFlush was called
        verify(bookingRepository, times(1)).saveAndFlush(booking);

        // Verify the result
        assertEquals(true, result);
        assertEquals(BookingStatus.ACCEPTED, booking.getBookingStatus());
        assertEquals(driver1.getDriver(), booking.getDriver());
    }

    private LastKnownLocation createDriver(String id, String latitude, String longitude) {
        LastKnownLocation loc = new LastKnownLocation();
		Location location = new Location();
        loc.setDriver(new Driver(UUID.fromString(id))); // Assuming Driver has an appropriate constructor
        location.setLatitude(latitude);
        location.setLongitude(longitude);
		loc.setLocation(location);
        return loc;
    }

    // Add more tests as needed
}
