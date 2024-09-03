package com.management.book.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.management.book.dtos.BookResponseDto;
import com.management.book.dtos.BorrowedBookResponseDto;
import com.management.book.models.Book;
import com.management.book.models.User;
import com.management.book.services.BookService;
import com.management.book.utils.PageResponse;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("books")
@RequiredArgsConstructor
@Tag(name = "Book")
public class BookController {

	private final BookService service;

	@PostMapping
	public ResponseEntity<Book> save(@RequestBody @Valid Book book, @AuthenticationPrincipal User user) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(book, user));
	}

	@GetMapping("/{bookId}")
	public ResponseEntity<BookResponseDto> getOne(@PathVariable("bookId") Long bookId) {
		return ResponseEntity.ok(service.getOne(bookId));
	}

	@GetMapping
	public ResponseEntity<PageResponse<BookResponseDto>> getAll(
			@RequestParam(name = "page", defaultValue = "0", required = false) int page,
			@RequestParam(name = "size", defaultValue = "20", required = false) int size,
			@AuthenticationPrincipal User user) {
		return ResponseEntity.ok(service.getAll(page, size, user));
	}

	@GetMapping("/owner")
	public ResponseEntity<PageResponse<BookResponseDto>> getAllByOwner(
			@RequestParam(name = "page", defaultValue = "0", required = false) int page,
			@RequestParam(name = "size", defaultValue = "20", required = false) int size,
			@AuthenticationPrincipal User user) {
		return ResponseEntity.ok(service.getAllByOwner(page, size, user));
	}

	@GetMapping("/borrowed")
	public ResponseEntity<PageResponse<BorrowedBookResponseDto>> getAllBorrowed(
			@RequestParam(name = "page", defaultValue = "0", required = false) int page,
			@RequestParam(name = "size", defaultValue = "20", required = false) int size,
			@AuthenticationPrincipal User user) {
		return ResponseEntity.ok(service.getAllBorrowed(page, size, user));
	}

	@GetMapping("/returned")
	public ResponseEntity<PageResponse<BorrowedBookResponseDto>> getAllReturned(
			@RequestParam(name = "page", defaultValue = "0", required = false) int page,
			@RequestParam(name = "size", defaultValue = "20", required = false) int size,
			@AuthenticationPrincipal User user) {
		return ResponseEntity.ok(service.getAllReturned(page, size, user));
	}

	@PatchMapping("/shareable/{bookId}")
	public ResponseEntity<Long> updateShareable(@PathVariable("bookId") Long bookId,
			@AuthenticationPrincipal User user) {
		return ResponseEntity.ok(service.updateShareable(bookId, user));
	}

	@PatchMapping("/archived/{bookId}")
	public ResponseEntity<Long> updateArchived(@PathVariable("bookId") Long bookId,
			@AuthenticationPrincipal User user) {
		return ResponseEntity.ok(service.updateArchived(bookId, user));
	}

	@PostMapping("/borrow/{bookId}")
	public ResponseEntity<Long> borrow(@PathVariable("bookId") Long bookId, @AuthenticationPrincipal User user) {
		return ResponseEntity.ok(service.borrow(bookId, user));
	}

	@PostMapping("/borrow/return/{bookId}")
	public ResponseEntity<Long> returnBorrowed(@PathVariable("bookId") Long bookId,
			@AuthenticationPrincipal User user) {
		return ResponseEntity.ok(service.returnBorrowed(bookId, user));
	}

	@PostMapping("/borrow/return/approve/{bookId}")
	public ResponseEntity<Long> approveReturnBorrowed(@PathVariable("bookId") Long bookId,
			@AuthenticationPrincipal User user) {
		return ResponseEntity.ok(service.approveReturnBorrowed(bookId, user));
	}

	@PostMapping(value = "/cover/{bookId}", consumes = "multipart/form-data")
	public ResponseEntity<?> uplodadCoverPicture(@PathVariable("bookId") Long bookId,
			@AuthenticationPrincipal User user, @RequestPart("file") MultipartFile file) {
		service.uplodadCoverPicture(file, user, bookId);
		return ResponseEntity.accepted().build();
	}
}
