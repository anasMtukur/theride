package com.anas.theride.lastknownlocation;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.anas.theride.driver.Driver;

@Repository
public interface LastKnownLocationRepository extends JpaRepository<LastKnownLocation, UUID> {
	LastKnownLocation findByDriver( Driver driver );

	@Query("Select lkl from LastKnownLocation lkl where lkl.location.geoHashZone = :geoHashZone")
    List<LastKnownLocation> findByLocationGeoHashZone(@Param( "geoHashZone" ) String geoHashZone);
}
