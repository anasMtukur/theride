package com.anas.theride.security.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.anas.theride.user.EndUser;
import com.anas.theride.user.EndUserRepository;

class JwtTokenServiceTest {

    @Mock
    private EndUserRepository endUserRepository;

    @InjectMocks
    private JwtTokenService jwtTokenService;

    private static final String USERNAME = "testUser";
    private static final String SECRET = "testSecret";
    private static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtTokenService = new JwtTokenService();
        jwtTokenService.secret = SECRET;
        jwtTokenService.endUserRepository = endUserRepository;

		this.setMockEndUser();
    }

    @Test
    void testGenerateToken() {
        
        UserDetails userDetails = new User(USERNAME, "password", Collections.emptyList());
        String token = jwtTokenService.generateToken(userDetails);

        assertNotNull(token);
        assertTrue(jwtTokenService.validateToken(token, userDetails));
    }

    @Test
    void testGetUsernameFromToken() {

        String token = jwtTokenService.generateToken(new User(USERNAME, "password", Collections.emptyList()));
        String username = jwtTokenService.getUsernameFromToken(token);

        assertEquals(USERNAME, username);
    }

    
	@Test
    void testGetExpirationDateFromToken() {

        String token = jwtTokenService.generateToken(new User(USERNAME, "password", Collections.emptyList()));
        Date expirationDate = jwtTokenService.getExpirationDateFromToken(token);

        assertNotNull(expirationDate);
    }

    @Test
    void testValidateToken() {

        String token = jwtTokenService.generateToken(new User(USERNAME, "password", Collections.emptyList()));
        UserDetails userDetails = new User(USERNAME, "password", Collections.emptyList());

        assertTrue(jwtTokenService.validateToken(token, userDetails));
    }
    
	private void setMockEndUser(){
		EndUser user = new EndUser();
        user.setUsername(USERNAME);
        user.setFullName("Test User");
        user.setMobile("123456789");

        when(endUserRepository.findByUsername(USERNAME)).thenReturn(user);
	}

}
