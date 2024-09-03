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
public class BookResponseDto {

	private Long id;
	private String title;
	private String authorName;
	private String isbn;
	private String synopsis;
	private String owner;
	private byte[] cover;
	private double rate;
	private boolean archived;
	private boolean shareable;
}
