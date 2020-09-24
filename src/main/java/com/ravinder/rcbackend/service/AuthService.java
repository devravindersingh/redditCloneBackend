package com.ravinder.rcbackend.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ravinder.rcbackend.dto.AuthenticationResponse;
import com.ravinder.rcbackend.dto.LoginRequest;
import com.ravinder.rcbackend.dto.RegisterRequest;
import com.ravinder.rcbackend.exception.SpringRedditException;
import com.ravinder.rcbackend.model.NotificationEmail;
import com.ravinder.rcbackend.model.User;
import com.ravinder.rcbackend.model.VerificationToken;
import com.ravinder.rcbackend.repository.UserRepository;
import com.ravinder.rcbackend.repository.VerificationTokenRepository;
import com.ravinder.rcbackend.security.JwtProvider;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {
	
	private final PasswordEncoder encoder;
	private final UserRepository userRepository;
	private final VerificationTokenRepository verificationRepo;
	private final MailService mailService;
	private final AuthenticationManager authenticationManager;
	private final JwtProvider jwtProvider;
	
	@Transactional
	public void signup(RegisterRequest registerRequest) {
			User user = new User();
			user.setUsername(registerRequest.getUsername());
			user.setPassword(encoder.encode(registerRequest.getPassword()));
			user.setEmail(registerRequest.getEmail());
			user.setCreated(Instant.now());
			user.setEnabled(false);
			userRepository.save(user);
			
			String token = generateVerificationToken(user);
			mailService.sendMail(new NotificationEmail(
					"Please Activate you Account", 
					registerRequest.getEmail(), 
					"ThankYou for signing up to Srping Reddit Clone, " + 
					"plase click on the below url to activate your account: " + 
					"http://localhost:8080/api/auth/accountVerification/" + token
			));
	}
	
	private String generateVerificationToken(User user) {
		String token = UUID.randomUUID().toString();
		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setToken(token);
		verificationToken.setUser(user);
		verificationRepo.save(verificationToken);
		return token;
	}

	public void verifyAccount(String token) {
		Optional<VerificationToken> verificationToken = verificationRepo.findByToken(token);
		verificationToken.orElseThrow(()-> new SpringRedditException("Invalid Token"));
		fetchUserAndEnable(verificationToken.get());
	}
	
	@Transactional
	private void fetchUserAndEnable(VerificationToken verificationToken) {
		String username = verificationToken.getUser().getUsername();
		User user = userRepository.findByUsername(username).orElseThrow(()-> new SpringRedditException("User not found with name " + username));
		user.setEnabled(true);
		userRepository.save(user);
		
	}

	public AuthenticationResponse login(LoginRequest loginRequest) {
		Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authenticate);
		String token = jwtProvider.generateToken(authenticate);
		return new AuthenticationResponse(token, loginRequest.getUsername());
	}
}
