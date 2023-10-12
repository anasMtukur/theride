package com.anas.theride.booking;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.anas.theride.audit.AuditEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "booking_points")
public class BookingPoint  extends AuditEntity{
	@JoinColumn(name = "booking", referencedColumnName = "id")
    @OneToOne(optional = false)
	@JsonBackReference
	private Booking booking;

	@Enumerated(EnumType.STRING)
	@NotNull
	@Size(min = 1, max = 255)
	@Column(name = "point_type")
    private BookingPointType pointType;

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

	@NotNull
	@Size(min = 1, max = 255)
	@Column(name = "address")
    private String address;
}
