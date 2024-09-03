package com.management.book.services;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.management.book.exceptions.EntityNotFoundException;
import com.management.book.exceptions.OperationNotPermittedException;
import com.management.book.models.Book;
import com.management.book.models.Feedback;
import com.management.book.models.User;
import com.management.book.repositories.BookRepository;
import com.management.book.repositories.FeedbackRepository;
import com.management.book.utils.PageResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedbackService {

	private final FeedbackRepository repository;
	private final BookRepository bookRepository;

	public Feedback save(Feedback feedback, User user) {
		Book book = bookRepository.findById(feedback.getBook().getId())
				.orElseThrow(() -> new EntityNotFoundException("Livro não encontrado"));

		if (book.isArchived() || !book.isShareable()) {
			throw new OperationNotPermittedException(
					"Não é possivel deixar um feedback para um livro não compartilhado ou arquivado.");
		}

		if (Objects.equals(user.getId(), book.getOwner().getId())) {
			throw new OperationNotPermittedException("Você não pode dar um feedback em seu proprio livro");
		}

		feedback.setBook(book);

		return repository.save(feedback);
	}

	public PageResponse<Feedback> findAllByBook(Long bookId, int page, int size, Long userId) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Feedback> feedBackPage = repository.findAllByBookId(bookId, pageable);
		List<Feedback> feedbacks = feedBackPage.stream().map(f -> {
			f.setOwnFeedback(Objects.equals(f.getCreatedBy(), userId));
			return f;
		}).toList();
		
		
		return new PageResponse<>(
			feedbacks,
			feedBackPage.getNumber(), feedBackPage.getSize(), feedBackPage.getTotalElements(),
			feedBackPage.getTotalPages(), feedBackPage.isFirst(), feedBackPage.isLast()
		);
	}

}
