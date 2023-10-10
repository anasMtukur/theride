package com.anas.theride.authlog;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.anas.theride.audit.AuditEntity;
import com.anas.theride.user.EndUser;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Anas M. Tukur <anasmtukur91@gmail.com>
 */

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "log_login")
public class LogLogin extends AuditEntity {
	private static final long serialVersionUID = 1L;

	@JoinColumn(name = "end_user", referencedColumnName = "id")
	@ManyToOne(optional = false)
	private EndUser endUser;
}
