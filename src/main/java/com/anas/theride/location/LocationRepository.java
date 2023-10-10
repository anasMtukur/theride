package com.anas.theride.location;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.anas.theride.driver.Driver;

@Repository
public interface LocationRepository extends JpaRepository<Location, UUID>{

	@Query("SELECT l FROM Location l WHERE l.driver = :driver ORDER BY l.createdAt DESC")
    Page<Location> findLatestLocationForDriver(@Param("driver") Driver driver, Pageable pageable);
	
}
