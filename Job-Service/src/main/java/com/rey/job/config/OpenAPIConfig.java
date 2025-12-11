package com.rey.job.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info =@Info(
                contact = @Contact(
                        name = "BEN & CO",
                        email = "benandco99@gmail.com",
                        url = "https://ben&co.com"
                ),
                description = "OpenApi documentation for Job Service",
                title = "OpenApi Specification - BEN & CO",
                version = "1.0",
                license = @License(
                        name = "License name",
                        url = "https://some-url.co"
                ),
                termsOfService = "Terms of Service"
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url ="http://localhost:5050/"
                ),
                @Server(
                        description = "Prod ENV",
                        url = "https://ben&co.com"
                )
        }
)
public class OpenAPIConfig {
}
