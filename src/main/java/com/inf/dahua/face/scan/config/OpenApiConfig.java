package com.inf.dahua.face.scan.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public-apis")
                .pathsToMatch("/api/**")
                .build();
    }

    @Bean
    public OpenAPI faceScanOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Innoflex-Dahua API Integration")
                        .description("API documentation for DSS Application")
                        .version("v0.0.1")
                        .contact(new Contact()
                                .name("Bee Dev Team")
                                .email("hellowrold@example.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
