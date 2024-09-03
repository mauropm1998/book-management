package com.management.book.controllers.exceptions;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.management.book.exceptions.EntityNotFoundException;
import com.management.book.exceptions.OperationNotPermittedException;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> handleGlobalException(Exception e) {
    	e.printStackTrace();
    	
    	StandardError error = new StandardError();
		error.setTimestamp(Instant.now());
		error.setError("Erro interno, contacte o administrador para receber assistência");
		error.setMessage(e.getMessage());
		error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
	
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<StandardError> entityNotFound (EntityNotFoundException e, HttpServletRequest request) {
		StandardError error = new StandardError();
		error.setTimestamp(Instant.now());
		error.setError("Recurso não encontrado");
		error.setMessage(e.getMessage());
		error.setStatus(HttpStatus.NOT_FOUND.value());
		error.setPath(request.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	
	@ExceptionHandler(OperationNotPermittedException.class)
	public ResponseEntity<StandardError> operationNotPermitted (OperationNotPermittedException e, HttpServletRequest request) {
		StandardError error = new StandardError();
		error.setTimestamp(Instant.now());
		error.setError("Operação não permitida");
		error.setMessage(e.getMessage());
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setPath(request.getRequestURI());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
	
	@ExceptionHandler(LockedException.class)
	public ResponseEntity<StandardError> accountLocked (LockedException e) {
		StandardError error = new StandardError();
		error.setTimestamp(Instant.now());
		error.setError("Conta Bloqueada");
		error.setMessage("O acesso a essa conta está atualmente bloqueado");
		error.setStatus(300);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
	}
	
	@ExceptionHandler(DisabledException.class)
	public ResponseEntity<StandardError> disabledAccount (DisabledException e) {
		StandardError error = new StandardError();
		error.setTimestamp(Instant.now());
		error.setError("Conta Não Habilitada");
		error.setMessage("Essa conta ainda não foi verificada ou ativada");
		error.setStatus(301);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<StandardError> badCredentials (BadCredentialsException e) {
		StandardError error = new StandardError();
		error.setTimestamp(Instant.now());
		error.setError("Credenciais Incorretas");
		error.setMessage("Email ou palavra-passe incorretos");
		error.setStatus(302); 
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
	}
	
	@ExceptionHandler(MessagingException.class)
	public ResponseEntity<StandardError> mailErrors (MessagingException e) {
		StandardError error = new StandardError();
		error.setTimestamp(Instant.now());
		error.setError("Falha no Envio de Email");
		error.setMessage(e.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}
	
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<StandardError> invalidAttributes (MethodArgumentNotValidException e, HttpServletRequest request) {
		Set<String> errors = new HashSet<>();
		e.getBindingResult().getAllErrors().forEach(error -> {
			var errorMessage = error.getDefaultMessage();
			errors.add(errorMessage);
		});
		
		StandardError error = new StandardError();
		error.setTimestamp(Instant.now());
		error.setError("Campos com valores inválidos.");
		error.setStatus(304);
		error.setPath(request.getRequestURI());
		error.setErrors(errors);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
	
	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	public ResponseEntity<StandardError> entityDuplicateValues (SQLIntegrityConstraintViolationException e, HttpServletRequest request) {
		StandardError error = new StandardError();
		error.setTimestamp(Instant.now());
		error.setError("Campos duplicados. Já existe uma entidade com os mesmos dados informados.");
		error.setMessage(e.getMessage());
		error.setStatus(HttpStatus.CONFLICT.value());
		error.setPath(request.getRequestURI());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
	}

}

