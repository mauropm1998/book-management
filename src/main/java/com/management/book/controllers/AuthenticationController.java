package com.management.book.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.management.book.dtos.AuthenticationDto;
import com.management.book.dtos.AuthenticationResponseDto;
import com.management.book.dtos.RegistrationDto;
import com.management.book.services.AuthenticationService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {
	
	private final AuthenticationService service;
	
	@PostMapping("/register")
	@ResponseStatus(value = HttpStatus.ACCEPTED)
	public ResponseEntity<?> register (@RequestBody @Valid RegistrationDto registrationDto) throws MessagingException {
		service.register(registrationDto);
		return ResponseEntity.accepted().build();
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponseDto> authenticate (@RequestBody @Valid AuthenticationDto request) {
		return ResponseEntity.ok(service.authenticate(request));
	}
	
	@GetMapping("/activate-account")
	public void activateAccount(@RequestParam String token) throws MessagingException {
		service.activateAccount(token);
	}
}
