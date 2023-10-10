package com.anas.theride.user;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.anas.theride.enduserrole.EndUserRole;
import com.anas.theride.enduserrole.EndUserRoleRepository;
import com.anas.theride.role.Role;
import com.anas.theride.role.RoleName;
import com.anas.theride.role.RoleRepository;

@Component
public class EndUserService {
	
	@Autowired
	PasswordEncoder encoder;
	
	@Autowired
	EndUserRoleRepository enduserRoleRepo;
	
	@Autowired
	RoleRepository roleRepository;
	
	public Payload fromEndUser( EndUser endUser ) {
		Payload payload = new Payload();
		List<EndUserRole> endUserRoles = enduserRoleRepo.findByEndUser( endUser );
		List<RoleName> roles = new ArrayList<RoleName>();
		for( EndUserRole eur : endUserRoles ) {
			roles.add( eur.getRole().getName() );
		}
		
		payload.setId( String.valueOf( endUser.getId() ) );
		payload.setFullName( endUser.getFullName() );
		payload.setUsername( endUser.getUsername() );
		payload.setEmail( endUser.getEmail() );
		payload.setMobile( endUser.getMobile() );
		payload.setBlockCode( endUser.getBlockCode() );
		payload.setBlocked( endUser.isBlocked() );
		payload.setRoles( roles );
		
		return payload;
	}
	
	public EndUser fromPayload( Payload payload ) {
		EndUser endUser = new EndUser();
		if( payload.getId() != null ) {
			endUser.setId( UUID.fromString( payload.getId() ) );
		}
		endUser.setFullName( payload.getFullName() );
		endUser.setUsername( payload.getUsername() );
		endUser.setEmail( payload.getEmail() );
		endUser.setMobile( payload.getMobile() );
		endUser.setPassword( encoder.encode( payload.getPassword() ) );
		
		return endUser;
	}
	
	public List<EndUserRole> addEndUserRoles( EndUser endUser, List<RoleName> payloadRoles ){
		List<RoleName> roles = new ArrayList<RoleName>();
		roles.add( RoleName.ROLE_USER );
		roles.addAll( payloadRoles );
		
		List<EndUserRole> endUserRoles = new ArrayList<EndUserRole>();
		for( RoleName rn : roles ) {
			Role role = roleRepository.findByName( rn );
			endUserRoles.add( new EndUserRole( endUser,  role ) );
		}
		
		return enduserRoleRepo.saveAllAndFlush( endUserRoles );
	}

	public List<RoleName> getEndUserRoles( EndUser endUser ) {
		List<RoleName> roles = new ArrayList<RoleName>();

		List<EndUserRole> endUserRoles = enduserRoleRepo.findByEndUser( endUser );
		for( EndUserRole eur : endUserRoles ) {
			roles.add( eur.getRole().getName() );
		}
		
		return roles;
	}
}
