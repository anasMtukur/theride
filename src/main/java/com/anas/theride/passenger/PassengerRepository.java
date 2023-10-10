package com.anas.theride.passenger;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anas.theride.user.EndUser;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, UUID> {

	Optional<Passenger> findByEndUser(EndUser endUser);
}
