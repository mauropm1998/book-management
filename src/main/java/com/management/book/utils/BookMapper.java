package com.management.book.utils;

import org.springframework.stereotype.Service;

import com.management.book.dtos.BookResponseDto;
import com.management.book.dtos.BorrowedBookResponseDto;
import com.management.book.models.Book;
import com.management.book.models.BookTransactionHistory;

@Service
public class BookMapper {
	
	public BookResponseDto toBookResponse (Book book) {
		return BookResponseDto.builder()
				.id(book.getId())
				.title(book.getTitle())
				.authorName(book.getAuthorName())
				.isbn(book.getIsbn())
				.synopsis(book.getSynopsis())
				.rate(book.getRate())
				.archived(book.isArchived())
				.shareable(book.isShareable())
				.owner(book.getOwner().getFullname())
				.cover(FileUtils.readFileFromLocation(book.getBookCover()))
				.build();
	}
	
	public BorrowedBookResponseDto toBorrowedBookResponse (BookTransactionHistory history) {
		return BorrowedBookResponseDto.builder()
				.id(history.getBook().getId())
				.title(history.getBook().getTitle())
				.authorName(history.getBook().getAuthorName())
				.isbn(history.getBook().getIsbn())
				.returnApproved(history.isReturnApproved())
				.returned(history.isReturned())
				.build();
	}

}
