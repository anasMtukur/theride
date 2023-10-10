package com.anas.theride.lastknownlocation;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.anas.theride.audit.AuditEntity;
import com.anas.theride.driver.Driver;
import com.anas.theride.location.Location;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "last_known_locations")
public class LastKnownLocation  extends AuditEntity{
	@JoinColumn(name = "driver", referencedColumnName = "id")
    @OneToOne(optional = false)
	//@JsonIgnore
	@JsonManagedReference
    private Driver driver;

	@JoinColumn(name = "location", referencedColumnName = "id")
    @OneToOne(optional = false)
	@JsonIgnoreProperties({"driver"})
    private Location location;

	@Transient
	double distance;
}
