package com.anas.theride.passenger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.anas.theride.audit.AuditEntity;
import com.anas.theride.driver.Gender;
import com.anas.theride.user.EndUser;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "passengers")
public class Passenger   extends AuditEntity{
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
	@Size(min = 1, max = 20)
	@Column(name = "gender")
    private Gender gender;
}
