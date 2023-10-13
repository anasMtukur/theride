package com.anas.theride.driver;

import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.anas.theride.audit.AuditEntity;
import com.anas.theride.car.Car;
import com.anas.theride.exceptions.InvalidDriverStatusActivityException;
import com.anas.theride.lastknownlocation.LastKnownLocation;
import com.anas.theride.location.Location;
import com.anas.theride.location.LocationService;
import com.anas.theride.user.EndUser;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

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
@Table(name = "drivers")
public class Driver  extends AuditEntity{
	public Driver(UUID id){
		this.id = id;
	}
	@JoinColumn(name = "end_user", referencedColumnName = "id")
    @OneToOne(optional = false)
	@JsonIgnore
    private EndUser endUser;

	@NotNull
	@Size(min = 1, max = 100)
	@Column(name = "first_name")
	private String firstName;

	@NotNull
	@Size(min = 1, max = 100)
	@Column(name = "last_name")
    private String lastName;

	@NotNull
	@Size(min = 1, max = 100)
	@Column(name = "gender")
    private Gender gender;

	@NotNull
	@Size(min = 1, max = 255)
	@Column(name = "license_number")
    private String LicenseNumber;

    @Enumerated(EnumType.STRING)
	@NotNull
	@Size(min = 1, max = 255)
	@Column(name = "approval_status")
    private DriverApprovalStatus approvalStatus;

	@NotNull
	@Size(min = 1, max = 255)
	@Column(name = "active_city")
    private String activeCity;

	@Transient
	private Boolean isAvailable;

	@Transient
	@JsonProperty("endUserId")
	private UUID endUserId;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "driver")
	@JsonManagedReference
    private Car car;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "driver")
	@JsonBackReference
    private LastKnownLocation lastKnownLocation;

    public void setAvailable(Boolean available) throws InvalidDriverStatusActivityException {
        if (approvalStatus.equals(DriverApprovalStatus.DENIED)) {
            throw new InvalidDriverStatusActivityException( approvalStatus );
        }
        isAvailable = available;
    }

	public UUID getEndUserId() {
        return (endUser != null) ? endUser.getId() : null;
    }

    public void setEndUserId(UUID endUserId) {
        this.endUserId = endUserId;
    }
}
