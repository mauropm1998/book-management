package com.management.book.models;

import java.util.List;

import com.management.book.utils.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "tb_books")
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Book extends BaseEntity {
	
	@NotEmpty(message = "O título é obrigatório")
	@NotBlank(message = "O título é obrigatório")
	private String title;
	@NotEmpty(message = "O autor é obrigatório")
	@NotBlank(message = "O autor é obrigatório")
	private String authorName;
	@NotEmpty(message = "O código ISBN é obrigatório")
	@NotBlank(message = "O código ISBN é obrigatório")
	private String isbn;
	private String synopsis;
	private String bookCover;
	private boolean archived;
	private boolean shareable;
	
	@ManyToOne
	private User owner;
	@OneToMany(mappedBy = "book")
	private List<Feedback> feedbacks;
	@OneToMany(mappedBy = "book")
	private List<BookTransactionHistory> histories;
	
	@Transient
	public double getRate () {
		if(feedbacks == null || feedbacks.isEmpty()) {
			return 0.0;
		}
		
		var rate = this.feedbacks.stream().mapToDouble(Feedback::getNote).average().orElse(0.0);
		
		return Math.round(rate * 10.0) / 10.0;
	}

}
