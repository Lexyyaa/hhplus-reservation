package com.hhplus.reservation.support.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Reservation API")
                        .version("1.0.0")
                        .description("공연 예약 시스템 API 명세서"))
                .addSecurityItem(new SecurityRequirement().addList("queueToken"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("queueToken", new SecurityScheme()
                                .name("Authorization")
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .description("커스텀 UUID 토큰")));
    }
}