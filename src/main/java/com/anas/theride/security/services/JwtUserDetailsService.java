package com.anas.theride.security.services;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.anas.theride.enduserrole.EndUserRole;
import com.anas.theride.enduserrole.EndUserRoleRepository;
import com.anas.theride.role.RoleName;
import com.anas.theride.user.EndUser;
import com.anas.theride.user.EndUserRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	private EndUserRepository endUserRepository;
	
	@Autowired
	private EndUserRoleRepository endUserRoleRepository;
	
	@Override
	public User loadUserByUsername(String username) throws UsernameNotFoundException {
		EndUser user = isValidEmailAddress(username) ? endUserRepository.findByEmail(username) : endUserRepository.findByMobile(username);
		
		if (user == null) {
			throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
		} else if (user.isBlocked() && user.isDeleted()) {
			throw new DisabledException("Your account has been disabled");
		} else {
			return toUser(user);
		}
	}
	
	private User toUser(EndUser endUser) {
		Collection<SimpleGrantedAuthority> authorities = new HashSet<>();
		List<EndUserRole> roles = endUserRoleRepository.findByEndUser(endUser);
		
		//Normal Users
		if ( checkUserHasRole( roles, RoleName.ROLE_USER ) ) {
			authorities.add(new SimpleGrantedAuthority("USER"));
		}
		
		//Driver Administrator
		if ( checkUserHasRole( roles, RoleName.ROLE_DRIVER ) ) {
			authorities.add(new SimpleGrantedAuthority("DRIVER"));
		}
		
		if ( checkUserHasRole( roles, RoleName.ROLE_ADMIN ) ) {
			authorities.add(new SimpleGrantedAuthority("ADMIN"));
		}
		
		if ( checkUserHasRole( roles, RoleName.ROLE_SUPER_ADMIN ) ) {
			authorities.add(new SimpleGrantedAuthority("SUPER_ADMIN"));
		}
		
		return new User(endUser.getUsername(), endUser.getPassword(), authorities);
	}
	
	public static boolean isValidEmailAddress(String email) {
		boolean result = true;
		try {
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
		} catch (AddressException ex) {
			result = false;
		}
		return result;
	}
	
	private static boolean checkUserHasRole( List<EndUserRole> roles, RoleName roleName ) {
		return roles.stream()
				.filter( r -> r.getRole().getName() == roleName ).collect( Collectors.toList()).size() > 0;
	}

}
