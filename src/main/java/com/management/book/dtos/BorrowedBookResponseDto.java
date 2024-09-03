package com.management.book.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BorrowedBookResponseDto {
	private Long id;
	private String title;
	private String authorName;
	private String isbn;
	private double rate;

	private boolean returned;
	private boolean returnApproved;
}
