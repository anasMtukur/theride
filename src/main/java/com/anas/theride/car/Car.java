package com.anas.theride.car;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.anas.theride.audit.AuditEntity;
import com.anas.theride.driver.Driver;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "cars")
public class Car extends AuditEntity{
    @JoinColumn(name = "driver", referencedColumnName = "id")
    @OneToOne(optional = false)
	@JsonBackReference
    private Driver driver;

	@Enumerated(EnumType.STRING)
	@NotNull
	@Size(min = 1, max = 255)
	@Column(name = "color")
    private Color color;

	@NotNull
	@Size(min = 1, max = 255)
	@Column(name = "brand_model")
    private String brandAndModel;

	@NotNull
	@Size(min = 1, max = 255)
	@Column(name = "plate_no")
    private String plateNo;

    @Enumerated(EnumType.STRING)
	@NotNull
	@Size(min = 1, max = 255)
	@Column(name = "car_type")
    private CarType carType;

}
