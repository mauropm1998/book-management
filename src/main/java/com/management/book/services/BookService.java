package com.management.book.services;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.management.book.dtos.BookResponseDto;
import com.management.book.dtos.BorrowedBookResponseDto;
import com.management.book.exceptions.EntityNotFoundException;
import com.management.book.exceptions.OperationNotPermittedException;
import com.management.book.models.Book;
import com.management.book.models.BookTransactionHistory;
import com.management.book.models.User;
import com.management.book.repositories.BookRepository;
import com.management.book.repositories.BookTransactionHistoryRepository;
import com.management.book.utils.BookMapper;
import com.management.book.utils.PageResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {

	private final BookRepository repository;
	private final BookMapper bookMapper;
	private final BookTransactionHistoryRepository transactionHistoryRepository;
	private final FileStorageService fileStorageService;

	@Transactional
	public Book save(Book book, User user) {
		// Setting some values to User object and return it
		book.setOwner(user);
		return repository.save(book);
	}

	public BookResponseDto getOne(Long bookId) {
		// Get the specified Book and return it
		BookResponseDto book = repository.findById(bookId).map(bookMapper::toBookResponse)
				.orElseThrow(() -> new EntityNotFoundException("Livro não encontrado"));
		return book;
	}

	public PageResponse<BookResponseDto> getAll(int page, int size, User user) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
		Page<Book> bookPage = repository.findAllAvailable(pageable, user.getId());
		List<BookResponseDto> books = bookPage.stream().map(bookMapper::toBookResponse).toList();

		return new PageResponse<>(books, bookPage.getNumber(), bookPage.getSize(), bookPage.getTotalElements(),
				bookPage.getTotalPages(), bookPage.isFirst(), bookPage.isLast());
	}

	public PageResponse<BookResponseDto> getAllByOwner(int page, int size, User user) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
		Page<Book> bookPage = repository.findAllByOwner(pageable, user.getId());
		List<BookResponseDto> books = bookPage.stream().map(bookMapper::toBookResponse).toList();

		return new PageResponse<>(books, bookPage.getNumber(), bookPage.getSize(), bookPage.getTotalElements(),
				bookPage.getTotalPages(), bookPage.isFirst(), bookPage.isLast());
	}

	public PageResponse<BorrowedBookResponseDto> getAllBorrowed(int page, int size, User user) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
		Page<BookTransactionHistory> allBorrowed = transactionHistoryRepository.findAllBorrowed(pageable, user.getId());
		List<BorrowedBookResponseDto> books = allBorrowed.stream().map(bookMapper::toBorrowedBookResponse).toList();

		return new PageResponse<>(books, allBorrowed.getNumber(), allBorrowed.getSize(), allBorrowed.getTotalElements(),
				allBorrowed.getTotalPages(), allBorrowed.isFirst(), allBorrowed.isLast());
	}

	public PageResponse<BorrowedBookResponseDto> getAllReturned(int page, int size, User user) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
		Page<BookTransactionHistory> allBorrowed = transactionHistoryRepository.findAllReturned(pageable, user.getId());
		List<BorrowedBookResponseDto> books = allBorrowed.stream().map(bookMapper::toBorrowedBookResponse).toList();

		return new PageResponse<>(books, allBorrowed.getNumber(), allBorrowed.getSize(), allBorrowed.getTotalElements(),
				allBorrowed.getTotalPages(), allBorrowed.isFirst(), allBorrowed.isLast());
	}

	public Long updateShareable(Long bookId, User user) {
		Book book = repository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Livro não encontrado"));

		if (!Objects.equals(user.getId(), book.getOwner().getId())) {
			throw new OperationNotPermittedException("Você não pode atualizar o estado de outros livros");
		}

		book.setShareable(!book.isShareable());
		repository.save(book);

		return bookId;
	}

	public Long updateArchived(Long bookId, User user) {
		Book book = repository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Livro não encontrado"));

		if (!Objects.equals(user.getId(), book.getOwner().getId())) {
			throw new OperationNotPermittedException("Você não pode atualizar o estado de outros livros");
		}

		book.setArchived(!book.isArchived());
		repository.save(book);

		return bookId;
	}

	public Long borrow(Long bookId, User user) {
		Book book = repository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Livro não encontrado"));

		if (book.isArchived() || !book.isShareable()) {
			throw new OperationNotPermittedException("Este livro está arquivado ou não compartilhado.");
		}

		if (Objects.equals(user.getId(), book.getOwner().getId())) {
			throw new OperationNotPermittedException("Você não pode pedir emprestado seu proprio livro");
		}

		final boolean isAlreadyBorrowed = transactionHistoryRepository.isAlreadyBorrowedByUser(bookId, user.getId());

		if (isAlreadyBorrowed) {
			throw new OperationNotPermittedException("Este livro já se encontra emprestado");
		}

		BookTransactionHistory history = BookTransactionHistory.builder().user(user).book(book).returned(false)
				.returnApproved(false).build();

		return transactionHistoryRepository.save(history).getId();
	}

	public Long returnBorrowed(Long bookId, User user) {
		Book book = repository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Livro não encontrado"));

		if (book.isArchived() || !book.isShareable()) {
			throw new OperationNotPermittedException("Este livro está arquivado ou não compartilhado.");
		}

		if (Objects.equals(user.getId(), book.getOwner().getId())) {
			throw new OperationNotPermittedException("Você não pode retornar ou pedir emprestado seu proprio livro");
		}

		BookTransactionHistory history = transactionHistoryRepository.findByBookIdAndUserId(bookId, user.getId())
				.orElseThrow(
						() -> new OperationNotPermittedException("Você não pediu esse livro, então podes retorná-lo"));

		history.setReturned(true);

		return transactionHistoryRepository.save(history).getId();
	}

	public Long approveReturnBorrowed(Long bookId, User user) {
		Book book = repository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Livro não encontrado"));

		if (book.isArchived() || !book.isShareable()) {
			throw new OperationNotPermittedException("Este livro está arquivado ou não compartilhado.");
		}

		if (!Objects.equals(user.getId(), book.getOwner().getId())) {
			throw new OperationNotPermittedException("Você não pode aprovar o retorno desse livro");
		}

		BookTransactionHistory history = transactionHistoryRepository.findByBookIdAndOwnerId(bookId, user.getId())
				.orElseThrow(
						() -> new OperationNotPermittedException("Livro ainda não retornado. Portanto, não pode ser aprovado"));

		history.setReturnApproved(true);

		return transactionHistoryRepository.save(history).getId();
	}

	public void uplodadCoverPicture(MultipartFile file, User user, Long bookId) {
		Book book = repository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Livro não encontrado"));	
		var bookCover = fileStorageService.saveFile(file, user.getId());
		book.setBookCover(bookCover);
		repository.save(book);
	}

}
