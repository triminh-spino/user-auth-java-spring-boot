package com.example.demo_testing.Config;

import java.util.List;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenAPIConfig {
    
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(
                new Info()
                    .title("API Document")
                    .version("v1.1")
                    .description("This is document for demo")
                    .license(
                        new License()
                            .name("API License")
                            .url("")   
                    )
            )
            .servers(
                List.of(
                    new Server()
                        .url("http://localhost:8080")
                        .description("Local server")
                )
            )
            .components(
                new Components()
                    .addSecuritySchemes(
                        "bearerAuth",
                        new SecurityScheme()
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                    )
            )
            .security(
                List.of(
                    new SecurityRequirement()
                        .addList("bearerAuth")
                )
            );
    }

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder()
            .group("demo-api")
            .packagesToScan("com.example.demo_testing.Controller")
            .build();
    }
}
