package com.management.book.models;

import com.management.book.utils.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "tb_feedbacks")
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Feedback extends BaseEntity {
	
	@Positive(message = "A nota precisa ser positiva")
	@NotNull(message = "Informe uma nota para esse livro")
	@Min(message = "O valor minimo deve ser 0", value = 0)
	@Max(message = "O valor máximo dever 5", value = 5)
	private Double note;
	@NotBlank(message = "O comentário é obrigatório")
	@NotEmpty(message = "O comentário é obrigatório")
	private String comment;	
	
	@NotNull(message = "O livro precisa ser informado")
	@ManyToOne
	private Book book;
	
	@Transient
	private boolean ownFeedback;
	
}
