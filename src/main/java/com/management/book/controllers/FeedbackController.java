package com.management.book.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.management.book.models.Feedback;
import com.management.book.models.User;
import com.management.book.services.FeedbackService;
import com.management.book.utils.PageResponse;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("feedbacks")
@RequiredArgsConstructor
@Tag(name = "Feedback")
public class FeedbackController {

	private final FeedbackService service;

	@PostMapping
	public ResponseEntity<Feedback> save(@Valid @RequestBody Feedback feedback, @AuthenticationPrincipal User user) {
		return ResponseEntity.ok(service.save(feedback, user));
	}

	@GetMapping("/book/{bookId}")
	public ResponseEntity<PageResponse<Feedback>> findAllByBook(@PathVariable("bookId") Long bookId,
			@RequestParam(name = "page", defaultValue = "0", required = false) int page,
			@RequestParam(name = "size", defaultValue = "20", required = false) int size,
			@AuthenticationPrincipal User user) {
		return ResponseEntity.ok(service.findAllByBook(bookId, page, size, user.getId()));
	}
}
