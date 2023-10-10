package com.anas.theride.booking;

import lombok.*;

import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.anas.theride.audit.AuditEntity;
import com.anas.theride.driver.Driver;
import com.anas.theride.passenger.Passenger;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking extends AuditEntity{

	@JoinColumn(name = "passenger", referencedColumnName = "id")
    @ManyToOne
    private Passenger passenger;

	@JoinColumn(name = "driver", referencedColumnName = "id")
    @ManyToOne
    private Driver driver;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "booking")
	@JsonManagedReference
	private List<BookingPoint> points;

    @Enumerated(EnumType.STRING)
	@NotNull
	@Size(min = 1, max = 255)
	@Column(name = "booking_type")
    private BookingType bookingType;

    @Enumerated(EnumType.STRING)
	@NotNull
	@Size(min = 1, max = 255)
	@Column(name = "status")
    private BookingStatus bookingStatus;

	@NotNull
	@Size(min = 1, max = 255)
	@Column(name = "geo_hash_zone")
    private String geoHashZone;

}
