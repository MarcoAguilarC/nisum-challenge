package com.nisum.challenge.infrastructure.out.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Parameter acceptLanguageHeader = new Parameter()
                .in("header")
                .required(false)
                .name("Accept-Language")
                .description("Preferred response language for responses. Examples: 'es' (Español), 'en' (English). Note: browsers may override this header. From Swagger UI, use the 'lang' query param instead.")
                .schema(new StringSchema()._default("es").example("es"));

        Components components = new Components()
                .addParameters("AcceptLanguageHeader", acceptLanguageHeader);

        return new OpenAPI().components(components);
    }
}
