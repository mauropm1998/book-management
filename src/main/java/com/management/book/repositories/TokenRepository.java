package com.management.book.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.management.book.models.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {
	Optional<Token> findByToken (String token);
}
