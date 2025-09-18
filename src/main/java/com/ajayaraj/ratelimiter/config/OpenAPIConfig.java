package com.ajayaraj.ratelimiter.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI myOpenAPI() {
        Info info = new Info()
                .title("RateLimiter")
                .version("1.0.0")
                .description("Rate limiter application using spring");

        Server server1 = new Server()
                .url("http://localhost:8080/")
                .description("Local development server");
        Server server2 = new Server()
                .url("http://localhost:8081/")
                .description("Dummy server");
        return new OpenAPI().info(info)
                .servers(List.of(server1, server2));
    }

}
