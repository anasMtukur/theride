package com.anas.theride.controllers;

import com.anas.theride.car.Car;
import com.anas.theride.car.CarRepository;
import com.anas.theride.car.CarType;
import com.anas.theride.car.Color;
import com.anas.theride.controllers.DriverOnboardingRequestController;
import com.anas.theride.driver.Driver;
import com.anas.theride.driver.DriverApprovalStatus;
import com.anas.theride.driver.DriverRepository;
import com.anas.theride.exceptions.DriverRequestAlreadySentException;
import com.anas.theride.exceptions.EntityNotFoundException;
import com.anas.theride.exceptions.InvalidValueException;
import com.anas.theride.payloads.DriverOnboardingRequestPayload;
import com.anas.theride.payloads.DriverOnboardingStatusUpdatePayload;
import com.anas.theride.payloads.PagedPayload;
import com.anas.theride.services.PagingUtil;
import com.anas.theride.user.EndUser;
import com.anas.theride.user.EndUserRepository;
import com.anas.theride.user.EndUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class DriverOnboardingRequestControllerTest {

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private CarRepository carRepository;

    @Mock
    private EndUserRepository endUserRepository;

    @Mock
    private EndUserService endUserService;

    @InjectMocks
    private DriverOnboardingRequestController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testRequest() throws Exception {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("username");

        EndUser endUser = new EndUser();
        endUser.setId(UUID.randomUUID());
		endUser.setFullName("Test User");
        endUser.setUsername("username");

        when(endUserRepository.findByUsername(eq("username"))).thenReturn(endUser);
        when(driverRepository.findByEndUser(eq(endUser))).thenReturn(Optional.empty());
		when(driverRepository.saveAndFlush(any())).thenReturn(new Driver(UUID.randomUUID()));

        DriverOnboardingRequestPayload payload = new DriverOnboardingRequestPayload();
        payload.setGender("MALE");
        payload.setCarType("SEDAN");
        payload.setCarColor("Red");
        payload.setCity("City");
        payload.setLicenseNumber("ABC123");
        payload.setCarBrand("Toyota");
        payload.setCarModel("Camry");
        payload.setCarPlateNo("XYZ987");

        ResponseEntity<Driver> response = controller.request(principal, payload);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(DriverApprovalStatus.PENDING, response.getBody().getApprovalStatus());

        verify(driverRepository, times(1)).saveAndFlush(any(Driver.class));
        //verify(carRepository, times(1)).saveAndFlush(any(Car.class));
    }

    @Test
    void testRequestWithDriverRequestAlreadySentException() throws Exception {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("username");

        EndUser endUser = new EndUser();
        endUser.setId(UUID.randomUUID());
        endUser.setUsername("username");
        when(endUserRepository.findByUsername(eq("username"))).thenReturn(endUser);

        Driver driver = new Driver();
        driver.setId(UUID.randomUUID());
        driver.setApprovalStatus(DriverApprovalStatus.PENDING);
        when(driverRepository.findByEndUser(eq(endUser))).thenReturn(Optional.of(driver));

        DriverOnboardingRequestPayload payload = new DriverOnboardingRequestPayload();
        payload.setGender("MALE");
        payload.setCarType("SEDAN");
        payload.setCarColor("RED");
        payload.setCity("City");
        payload.setLicenseNumber("ABC123");
        payload.setCarBrand("Toyota");
        payload.setCarModel("Camry");
        payload.setCarPlateNo("XYZ987");

        assertThrows(DriverRequestAlreadySentException.class, () -> controller.request(principal, payload));

        verify(driverRepository, never()).saveAndFlush(any(Driver.class));
        verify(carRepository, never()).saveAndFlush(any(Car.class));
    }

    @Test
    void testFindWaiting() throws Exception {
        int pageNumber = 0;
        int pageSize = 10;
        String sortBy = "date";
        String sortDirection = "desc";

        Page<Driver> page = mock(Page.class);
        when(driverRepository.findByApprovalStatus(eq(DriverApprovalStatus.PENDING), any())).thenReturn(page);
        when(page.getTotalPages()).thenReturn(1);
        when(page.getTotalElements()).thenReturn(1L);
        when(page.getContent()).thenReturn(Collections.singletonList(new Driver()));

        ResponseEntity<PagedPayload<Driver>> response = controller.findWaiting(mock(Principal.class),
                pageNumber, "", "", pageSize, sortBy, sortDirection, "");

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalPages());
        assertEquals(1, response.getBody().getTotalElements());
        assertEquals(1, response.getBody().getItems().size());
    }

    @Test
    void testFindWaitingByPrincipal() throws Exception {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("username");

        EndUser endUser = new EndUser();
        endUser.setId(UUID.randomUUID());
        endUser.setUsername("username");
        when(endUserRepository.findByUsername(eq("username"))).thenReturn(endUser);

        Driver driver = new Driver();
        driver.setId(UUID.randomUUID());
        when(driverRepository.findByEndUser(eq(endUser))).thenReturn(Optional.of(driver));

        ResponseEntity<Driver> response = controller.findWaitingByPrincipal(principal);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    void testUpdateStatus() throws Exception {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("username");

        DriverOnboardingStatusUpdatePayload payload = new DriverOnboardingStatusUpdatePayload();
        payload.setDriverId(UUID.randomUUID().toString());
        payload.setStatus("APPROVED");

        EndUser requestingUser = new EndUser();
        when(endUserRepository.findByUsername(eq("username"))).thenReturn(requestingUser);

        Driver driver = new Driver();
        driver.setId(UUID.fromString(payload.getDriverId()));
        when(driverRepository.findById(any(UUID.class))).thenReturn(Optional.of(driver));

        ResponseEntity<Driver> response = controller.updateStatus(principal, payload);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(DriverApprovalStatus.APPROVED, response.getBody().getApprovalStatus());
    }
}
