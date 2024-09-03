package com.management.book.services;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.management.book.dtos.AuthenticationDto;
import com.management.book.dtos.AuthenticationResponseDto;
import com.management.book.dtos.RegistrationDto;
import com.management.book.enums.EmailTemplateName;
import com.management.book.exceptions.EntityNotFoundException;
import com.management.book.exceptions.InvalidTokenException;
import com.management.book.exceptions.TokenExpiredException;
import com.management.book.models.Token;
import com.management.book.models.User;
import com.management.book.repositories.RoleRepository;
import com.management.book.repositories.TokenRepository;
import com.management.book.repositories.UserRepository;
import com.management.book.security.JwtService;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

	private final RoleRepository repository;
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final TokenRepository tokenRepository;
	private final EmailService emailService;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;

	@Value("${application.security.mailing.frontend.activation-url}")
	private String activationUrl;

	public void register(RegistrationDto registrationDto) throws MessagingException {
		var userRole = repository.findByName("USER")
				.orElseThrow(() -> new EntityNotFoundException("ROLE não encontrada"));
		var user = User.builder().firstname(registrationDto.getFirstname()).lastname(registrationDto.getLastname())
				.email(registrationDto.getEmail()).password(passwordEncoder.encode(registrationDto.getPassword()))
				.accountLocked(true).enabled(false).roles(List.of(userRole)).build();
		userRepository.save(user);
		sendValidationEmail(user);
	}

	private void sendValidationEmail(User user) throws MessagingException {
		var newToken = generateAndSaveActivationToken(user);
		emailService.sendEmail(user.getEmail(), user.getFullname(), EmailTemplateName.ACTIVATE_ACCOUNT, activationUrl,
				newToken, "Ativação de Conta");
	}

	private String generateAndSaveActivationToken(User user) {
		// Generate a token
		String generatedToken = generateActivationCode(6);
		var token = Token.builder().token(generatedToken).createdAt(LocalDateTime.now())
				.expiresAt(LocalDateTime.now().plusMinutes(15)).user(user).build();
		tokenRepository.save(token);
		return generatedToken;
	}

	private String generateActivationCode(int length) {
		// Generate Random Code Based on Parameter Length
		String characters = "0123456789";
		StringBuilder codeBuilder = new StringBuilder();
		SecureRandom secureRandom = new SecureRandom();

		for (int i = 0; i < length; i++) {
			int randomIndex = secureRandom.nextInt(length);
			codeBuilder.append(characters.charAt(randomIndex));
		}

		return codeBuilder.toString();
	}

	public AuthenticationResponseDto authenticate(@Valid AuthenticationDto request) {
		var auth = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

		var claims = new HashMap<String, Object>();
		var user = ((User) auth.getPrincipal());
		claims.put("fullName", user.getFullname());
		var jwtToken = jwtService.generateToken(claims, user);

		return AuthenticationResponseDto.builder().token(jwtToken).build();
	}

	public void activateAccount(String token) throws MessagingException {
		Token tokenSaved = tokenRepository.findByToken(token)
				.orElseThrow(() -> new InvalidTokenException("Este token não é válido"));

		if (LocalDateTime.now().isAfter(tokenSaved.getExpiresAt())) {
			sendValidationEmail(tokenSaved.getUser());
			new TokenExpiredException(
					"Este token já expirou. Um novo token foi enviado para o mesmo endereço de email");
		}
		else {
			var user = userRepository.findById(tokenSaved.getUser().getId())
					.orElseThrow(() -> new UsernameNotFoundException("Utilizador não encontrado"));
			user.setEnabled(true);
			userRepository.save(user);
			tokenSaved.setValidatedAt(LocalDateTime.now());
			tokenRepository.save(tokenSaved);
		}
	}

}
