package com.example.core.web.config;

import com.example.api.reservation.ReservationStatusConverter;
import com.example.api.restaurantimage.controller.converter.RestaurantImageTypeConverter;
import com.example.core.oauth.controller.OauthServerTypeConverter;
import com.example.core.web.security.login.LoginMemberArgumentResolver;
import com.example.core.web.security.login.LoginOwnerArgumentResolver;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    @Value("${cors.pathPattern}")
    private String pathPattern;
    @Value("${cors.allowedOrigins}")
    private String[] allowedOrigins;

    private final LoginMemberArgumentResolver loginMemberArgumentResolver;
    private final LoginOwnerArgumentResolver loginOwnerArgumentResolver;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(pathPattern)
                .allowedOrigins(allowedOrigins)
                .allowedMethods(
                        HttpMethod.GET.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.PUT.name(),
                        HttpMethod.DELETE.name(),
                        HttpMethod.PATCH.name())
                .allowCredentials(true)
                .exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials");
//                .exposedHeaders("*");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new OauthServerTypeConverter());
        registry.addConverter(new ReservationStatusConverter());
        registry.addConverter(new RestaurantImageTypeConverter());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
        resolvers.add(loginOwnerArgumentResolver);
    }
}
