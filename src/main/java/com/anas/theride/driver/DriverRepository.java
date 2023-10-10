package com.anas.theride.driver;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anas.theride.user.EndUser;

@Repository
public interface DriverRepository extends JpaRepository<Driver, UUID> {

	Optional<Driver> findByEndUser(EndUser endUser);

	Page<Driver> findByApprovalStatus(DriverApprovalStatus pending, Pageable pageRequest);
}
