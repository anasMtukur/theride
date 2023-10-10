package com.anas.theride.role;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, UUID> {
	
	public Role findByName(RoleName name);

}
