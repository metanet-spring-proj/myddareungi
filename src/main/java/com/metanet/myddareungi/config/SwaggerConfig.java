package com.metanet.myddareungi.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("myddareungi API")
                .description("따릉이 데이터 분석 서비스 API 문서")
                .version("v1.0.0")
                .contact(new Contact()
                        .name("myddareungi")
                        .email("myddareungi@example.com"));
    }
}