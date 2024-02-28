package com.example.core.web.swagger;

import static com.example.core.web.security.jwt.JWTUtils.AUTHORIZATION_HEADER;
import static io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//http://15.164.89.177:8080/swagger-ui/index.html
//http://localhost:8080/swagger-ui/index.html
@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEMES_BEARER = "bearer";
    private static final String SECURITY_SCHEMES_BEARER_FORMAT = "JWT";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(components())
                .info(apiInfo())
                .addSecurityItem(securityRequirement());
    }

    private static Components components() {
        // SecuritySchemes 등록
        return new Components()
                .addSecuritySchemes(AUTHORIZATION_HEADER, new SecurityScheme()
                        .name(AUTHORIZATION_HEADER)
                        .type(HTTP) // HTTP 방식
                        .scheme(SECURITY_SCHEMES_BEARER)
                        .bearerFormat(SECURITY_SCHEMES_BEARER_FORMAT));
    }

    private SecurityRequirement securityRequirement() {
        // API 요청헤더에 인증정보 포함
        return new SecurityRequirement()
                .addList(AUTHORIZATION_HEADER);
    }

    private Info apiInfo() {
        return new Info()
                .title("인프런 캐치테이블")
                .description("읽기 전용이기 때문에 실행시 오류가 나타날 수 있습니다.<br>" +
                        "시간 형식은 2024-02-20T01:05:07 <br>" +
                        "이미지와 같이 보내는 형태라면 dto는 application/json, 이미지는 이미지 타입에 맞게 ex)image/jpg");
    }
}
