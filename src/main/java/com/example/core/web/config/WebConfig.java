package com.example.core.web.config;

import com.example.api.reservation.ReservationStatusConverter;
import com.example.api.restaurantimage.controller.converter.RestaurantImageTypeConverter;
import com.example.core.oauth.controller.OauthServerTypeConverter;
import com.example.core.web.security.login.LoginMemberArgumentResolver;
import com.example.core.web.security.login.LoginOwnerArgumentResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LoginMemberArgumentResolver loginMemberArgumentResolver;
    private final LoginOwnerArgumentResolver loginOwnerArgumentResolver;

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
