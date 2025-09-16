package com.luopc.platform.web.example.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocExampleConfig {

    @Bean
    public OpenAPI myExampleAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("这只是一个示例项目")
                        .description("Hello, 这是一个示例项目，用于演示如何在Spring Boot项目中集成Swagger3和OpenAPI文档。")
                        .version("v1.0.0")
                        .license(new License()
                                .name("许可协议")
                                .url("https://No.9527.top"))
                        .contact(new Contact()
                                .name("编号9527")
                                .email("No.9527@gmail.com")))
                .externalDocs(new ExternalDocumentation()
                        .description("编号9527的博客")
                        .url("https://No.9527.top"));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("api")
                .pathsToMatch("/api/**")
                .build();
    }


}
