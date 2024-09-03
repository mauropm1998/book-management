package com.management.book.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.management.book.models.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

	@Query("""
			SELECT book
			FROM Book book
			WHERE book.archived = false
			AND book.shareable = true
			AND book.owner.id != :userId
			""")
	Page<Book> findAllAvailable(Pageable pageable, Long userId);

	@Query("""
			SELECT book
			FROM Book book
			WHERE book.owner.id = :userId
			""")
	Page<Book> findAllByOwner(Pageable pageable, Long userId);

}
