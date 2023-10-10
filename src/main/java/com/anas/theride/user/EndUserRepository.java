package com.anas.theride.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EndUserRepository extends JpaRepository<EndUser, UUID>{
	
	public EndUser findByEmail(String email);
	
	public EndUser findByMobile(String mobile);
	
	public EndUser findByUsername(String username);

	public EndUser findByResetToken(String resetToken);

	public boolean existsByUsername(String username);

	public boolean existsByEmail(String email);
	
	public boolean existsByMobile(String mobile);

	public EndUser findByEmailAndResetToken(String email, String token);

}
