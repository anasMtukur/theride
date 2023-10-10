package com.anas.theride.enduserrole;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anas.theride.user.EndUser;

public interface EndUserRoleRepository extends JpaRepository<EndUserRole, UUID> {
	List<EndUserRole> findByEndUser(EndUser endUser);

}
