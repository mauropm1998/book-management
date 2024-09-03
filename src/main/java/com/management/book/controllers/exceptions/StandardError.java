package com.management.book.controllers.exceptions;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(value = Include.NON_EMPTY)
public class StandardError implements Serializable {
	private static final long serialVersionUID = 1L;
	private Instant timestamp;
	private int status;
	private String error;
	private Set<String> errors;
	private String message;
	private String path;
}

