package com.management.book.configurations;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition (
	info = @Info(
		contact = @Contact (
			name ="Mauro Mateus",
			email = "maurocristovao1998@gmail.com",
			url = "https://localhost:8080"
		),
		description = "OpenApi documentation for Spring Security",
		title = "OpenApi specification - Mauro",
		version = "1.0",
		license = @License(
			name = "Minha Licença",
			url = "https://minhalicensa.com"
		),
		termsOfService = "Termos do serviço"
	),
	servers = {
			@Server(
					description = "Local ENV",
					url = "http://localhost:8080/api/v1"
			),
			@Server(
					description = "Production ENV",
					url = "https://crm.siclic.ao/api/v1"
			)
	},
	security = {
			@SecurityRequirement(
					name = "bearerAuth"
			)
	}
)

@SecurityScheme(
		name = "bearerAuth",
		description = "JWT auth description",
		scheme = "bearer",
		type = SecuritySchemeType.HTTP,
		bearerFormat = "JWT",
		in = SecuritySchemeIn.HEADER
)

public class OpenApiConfig {

}
