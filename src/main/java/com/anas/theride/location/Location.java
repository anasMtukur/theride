package com.anas.theride.location;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.anas.theride.audit.AuditEntity;
import com.anas.theride.driver.Driver;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "locations")
public class Location extends AuditEntity{
	@JoinColumn(name = "driver", referencedColumnName = "id")
    @OneToOne
    private Driver driver;

	@NotNull
	@Size(min = 1, max = 255)
	@Column(name = "longitude")
    private String longitude;

	@NotNull
	@Size(min = 1, max = 255)
	@Column(name = "latitude")
    private String latitude;

	@NotNull
	@Size(min = 1, max = 255)
	@Column(name = "geo_hash_zone")
    private String geoHashZone;

}
