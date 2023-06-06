package com.fs.filemarket.api.global.config.openapi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPI(@Value("${springdoc.version}") String springdocVersion){
        Info info = new Info()
                .title("File Market")
                .version(springdocVersion)
                .description("File Market API");
        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
