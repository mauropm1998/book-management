package com.management.book.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class RegistrationDto {
	
	@NotEmpty(message = "Primeiro nome é obrigatório")
	@NotBlank(message = "Primeiro nome é obrigatório")
	private String firstname;
	@NotEmpty(message = "Último nome é obrigatório")
	@NotBlank(message = "Último nome é obrigatório")
	private String lastname;
	
	@Email(message = "Email mal formatado")
	@NotEmpty(message = "Email é obrigatório")
	@NotBlank(message = "Email é obrigatório")
	private String email;
	
	@NotEmpty(message = "Palavra-passe é obrigatório")
	@NotBlank(message = "Palavra-passe é obrigatório")
	@Size(min = 8, message = "Palavra-passe deve ter no minímo 8 caracteres")
	private String password;

}
