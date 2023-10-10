package com.anas.theride.enduserrole;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.anas.theride.audit.AuditEntity;
import com.anas.theride.role.Role;
import com.anas.theride.user.EndUser;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "enduser_role")
@Data
@EqualsAndHashCode(callSuper=false)
public class EndUserRole extends AuditEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@JoinColumn(name = "end_user", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private EndUser endUser;

    @JoinColumn(name = "role", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Role role;
    
    public EndUserRole() {
    	
    }
    
    public EndUserRole( EndUser endUser, Role role ) {
    	this.endUser = endUser;
    	this.role = role;
    }

}
