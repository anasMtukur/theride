package com.anas.theride.location;

import com.anas.theride.driver.Driver;
import com.anas.theride.driver.DriverRepository;
import com.anas.theride.exceptions.EntityNotFoundException;
import com.anas.theride.kafka.location.LocationUpdateData;
import com.anas.theride.lastknownlocation.LastKnownLocation;
import com.anas.theride.lastknownlocation.LastKnownLocationRepository;
import com.anas.theride.location.Location;
import com.anas.theride.location.LocationRepository;
import com.anas.theride.user.EndUser;
import com.anas.theride.user.EndUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LocationServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private EndUserRepository endUserRepository;

    @Mock
    private LastKnownLocationRepository lastKnownLocationRepository;

    @InjectMocks
    private LocationService locationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testFromEntity() {
        Location location = new Location();
        location.setId(UUID.randomUUID());
        location.setDriver(new Driver(UUID.randomUUID()));
        location.setLatitude("40.7128");
        location.setLongitude("-74.0060");
        location.setGeoHashZone("someGeoHash");

        Payload payload = locationService.fromEntity(location);

        assertEquals(String.valueOf(location.getId()), payload.getId());
        assertEquals(location.getDriver(), payload.getDriver());
        assertEquals(location.getLatitude(), payload.getLatitude());
        assertEquals(location.getLongitude(), payload.getLongitude());
        assertEquals(location.getGeoHashZone(), payload.getGeoHashZone());
    }

    @Test
    void testFromPayload() {
        Payload payload = new Payload();
        //payload.setId(String.valueOf( UUID.randomUUID() ));
        payload.setDriver(new Driver(UUID.randomUUID()));
        payload.setLatitude("40.7128");
        payload.setLongitude("-74.0060");
        payload.setGeoHashZone("dr5regw3ppyz");

        Driver driver = new Driver(UUID.randomUUID());
        Location location = locationService.fromPayload(payload, driver);

        assertNull(location.getId()); // ID is not set in this case
        assertEquals(driver, location.getDriver());
        assertEquals(payload.getLatitude(), location.getLatitude());
        assertEquals(payload.getLongitude(), location.getLongitude());
        assertEquals(payload.getGeoHashZone(), location.getGeoHashZone());
    }

    @Test
    void testGenerateGeoHash() {
        double latitude = 40.7128;
        double longitude = -74.0060;

        String geoHash = locationService.generateGeoHash(latitude, longitude);

        assertNotNull(geoHash);
        // Add more assertions based on the expected behavior of the generateGeoHash method
    }

    @Test
    void testFindLastKnownDriverLocation() {
        Driver driver = new Driver(UUID.randomUUID());
        Location expectedLocation = new Location();
        expectedLocation.setId(UUID.randomUUID());
        expectedLocation.setDriver(driver);
        expectedLocation.setLatitude("40.7128");
        expectedLocation.setLongitude("-74.0060");
        expectedLocation.setGeoHashZone("someGeoHash");

        when(locationRepository.findLatestLocationForDriver(eq(driver), any()))
                .thenReturn(new org.springframework.data.domain.PageImpl<>(List.of(expectedLocation)));

        Location actualLocation = locationService.findLastKnownDriverLocation(driver);

        assertEquals(expectedLocation, actualLocation);
    }

    @Test
    void testSaveLocation() {
        LocationUpdateData data = new LocationUpdateData();
        data.setUserId(UUID.randomUUID().toString());
        data.setLatitude("40.7128");
        data.setLongitude("-74.0060");

        EndUser endUser = new EndUser();
        endUser.setId(UUID.fromString(data.getUserId()));
        Driver driver = new Driver(UUID.randomUUID());

        when(endUserRepository.findById(any(UUID.class))).thenReturn(Optional.of(endUser));
        when(driverRepository.findByEndUser(eq(endUser))).thenReturn(Optional.of(driver));
        when(locationRepository.save(any(Location.class))).thenAnswer(invocation -> {
            Location location = invocation.getArgument(0);
            location.setId(UUID.randomUUID());
            return location;
        });

        String locationId = locationService.saveLocation(data);

        assertNotNull(locationId);
        verify(locationRepository, times(1)).save(any(Location.class));
        verify(lastKnownLocationRepository, times(1)).saveAndFlush(any(LastKnownLocation.class));
    }

    @Test
    void testSaveLocationWithUnknownUser() {
        LocationUpdateData data = new LocationUpdateData();
        data.setUserId(UUID.randomUUID().toString());
        data.setLatitude("40.7128");
        data.setLongitude("-74.0060");

        when(endUserRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> locationService.saveLocation(data));
    }

    @Test
    void testSaveLocationWithNoDriverRole() {
        LocationUpdateData data = new LocationUpdateData();
        data.setUserId(UUID.randomUUID().toString());
        data.setLatitude("40.7128");
        data.setLongitude("-74.0060");

        EndUser endUser = new EndUser();
        endUser.setId(UUID.fromString(data.getUserId()));

        when(endUserRepository.findById(any(UUID.class))).thenReturn(Optional.of(endUser));
        when(driverRepository.findByEndUser(eq(endUser))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> locationService.saveLocation(data));
    }

    // Add more tests as needed
}
