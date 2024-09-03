package com.management.book.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationDto {

	@Email(message = "Email mal formatado")
	@NotEmpty(message = "Email é obrigatório")
	@NotBlank(message = "Email é obrigatório")
	private String email;

	@NotEmpty(message = "Palavra-passe é obrigatório")
	@NotBlank(message = "Palavra-passe é obrigatório")
	@Size(min = 8, message = "Palavra-passe deve ter no minímo 8 caracteres")
	private String password;

}
