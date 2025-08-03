package com.fedordemin.segmentservice.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI segmentServiceOpenAPI() {
        Schema<?> badRequestSchema = new Schema<>()
                .type("object")
                .example(Map.of(
                        "message", "Неверный запрос",
                        "status", 400
                ))
                .addProperties("message", new Schema<>().type("string"))
                .addProperties("status", new Schema<>().type("integer"));

        Schema<?> notFoundSchema = new Schema<>()
                .type("object")
                .example(Map.of(
                        "message", "Ресурс не найден",
                        "status", 404
                ))
                .addProperties("message", new Schema<>().type("string"))
                .addProperties("status", new Schema<>().type("integer"));

        Components components = new Components()
                .addResponses("BadRequest", new ApiResponse()
                        .description("Неверный запрос")
                        .content(new Content()
                                .addMediaType("application/json",
                                        new MediaType().schema(badRequestSchema))))
                .addResponses("NotFound", new ApiResponse()
                        .description("Ресурс не найден")
                        .content(new Content()
                                .addMediaType("application/json",
                                        new MediaType().schema(notFoundSchema))));

        return new OpenAPI()
                .components(components)
                .info(new Info()
                        .title("Segment Service API")
                        .description("API для управления сегментами пользователей")
                        .version("1.0.0"));
    }
}