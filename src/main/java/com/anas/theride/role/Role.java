package com.anas.theride.role;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.anas.theride.audit.AuditEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Anas M. Tukur <anasmtukur91@gmail.com>
 */
@EqualsAndHashCode(callSuper = false)
@Entity
@Data
@Table(name = "roles")
public class Role extends AuditEntity{
	private static final long serialVersionUID = 1L;
	
	@Enumerated(EnumType.STRING)
	@NotNull
	@Size(min = 1, max = 255)
	@Column(name = "name")
	private RoleName name;

	public Role() {}

	public Role(RoleName name) {
		this.name = name;
	}
	
	public String toString() {
		return this.name.toString();
	}
}
