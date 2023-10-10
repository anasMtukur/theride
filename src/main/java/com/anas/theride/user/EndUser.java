package com.anas.theride.user;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

import com.anas.theride.audit.AuditEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Anas M. Tukur <anasmtukur91@gmail.com>
 */
@EqualsAndHashCode(callSuper = false)
@Entity
@Data
@Getter
@Setter
@Table(name = "end_user")
public class EndUser extends AuditEntity{
	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 255)
	@Column(name = "fullname")
	private String fullName;
	
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 255)
	@Column(name = "username")
	private String username;
	
	@Basic(optional = true)
	@Size(min = 1, max = 255)
	@Column(name = "mobile", nullable = true)
	private String mobile;
	
	@Basic(optional = true)
	@Size(min = 1, max = 255)
	@Column(name = "email", nullable = true)
	private String email;
	
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 255)
	@Column(name = "password")
	private String password;
	
	@Basic(optional = true)
	@Column(name = "reset_token", nullable = true)
	private String resetToken;
	
	@Basic(optional = true)
	@Column(name = "block_code", nullable = true)
	private String blockCode;
	
	@Basic(optional = true)
	@NotNull
	@Column(name = "is_deleted", columnDefinition = "boolean DEFAULT false NOT NULL", nullable = false)
	private boolean isDeleted;
	
	@Basic(optional = true)
	@NotNull
	@Column(name = "is_blocked", columnDefinition = "boolean DEFAULT false NOT NULL", nullable = false)
	private boolean isBlocked;
}