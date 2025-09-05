package com.nisum.challenge.infrastructure.out.config;


import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
public class OpenApiHeaderGlobalCustomiser {

    @Bean
    public OpenApiCustomizer addGlobalLanguageHeaders() {
        return openAPI -> {
            if (openAPI.getPaths() == null) return;

            Parameter acceptLanguage = new Parameter()
                    .in("header")
                    .required(false)
                    .name("Accept-Language")
                    .description("Preferred response language. Examples: 'es' (Español), 'en' (English). Note: browsers may override it.")
                    .schema(new io.swagger.v3.oas.models.media.StringSchema().example("en"));

            for (Map.Entry<String, PathItem> entry : openAPI.getPaths().entrySet()) {
                PathItem item = entry.getValue();
                item.readOperations().forEach(op -> {
                    List<Parameter> params = op.getParameters();
                    if (params == null) {
                        op.setParameters(new java.util.ArrayList<>());
                        params = op.getParameters();
                    }
                    boolean hasAcceptLang = params.stream().anyMatch(p -> "Accept-Language".equalsIgnoreCase(p.getName()));
                    boolean hasLang = params.stream().anyMatch(p -> "lang".equalsIgnoreCase(p.getName()));
                    if (!hasAcceptLang) params.add(acceptLanguage);
                });
            }
        };
    }
}
