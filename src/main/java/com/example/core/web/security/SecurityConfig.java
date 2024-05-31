package com.example.core.web.security;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.example.core.web.config.CorsConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CorsConfig corsConfig;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin().disable()
                .httpBasic().disable()
                .csrf().disable()
                .cors().configurationSource(corsConfig.corsConfigurationSource())
                .and()
                .headers().frameOptions().disable()

                .and()
                .sessionManagement().sessionCreationPolicy(STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/oauth/**", "/favicon.ico","/v3/api-docs/**", "/swagger-ui/**", "/restaurants/search/**",  "/**.js", "/css/**", "/image/**", "/libs/**", "/assets/**", "/design/**").permitAll()
//                .antMatchers("/**").permitAll()
                .anyRequest().authenticated();

        http.addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint) // 인증 예외 핸들러 등록
                .accessDeniedHandler(customAccessDeniedHandler); // 인가 예외 핸들러 등록
    }
}
