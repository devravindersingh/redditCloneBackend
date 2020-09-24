package com.ravinder.rcbackend.service;

import java.util.Collection;
import java.util.Optional;
import static java.util.Collections.singletonList;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ravinder.rcbackend.model.User;
import com.ravinder.rcbackend.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	
	private final UserRepository userRepo;
	
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> userOptional = userRepo.findByUsername(username);
		User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("Username Not found " + username ));
		
		return new org.springframework.security.core.userdetails.
				User(user.getUsername(), user.getPassword(), user.isEnabled(), true, true, true, getAuthorities("USER"));
	}

	private Collection<? extends GrantedAuthority> getAuthorities(String role) {
		return singletonList(new SimpleGrantedAuthority(role));
	}

}
