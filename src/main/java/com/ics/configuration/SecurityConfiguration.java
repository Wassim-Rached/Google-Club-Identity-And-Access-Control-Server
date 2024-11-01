package com.ics.configuration;

import com.ics.filters.JwtAuthenticationFilter;
import com.ics.handlers.CustomAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(false);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .cors(Customizer.withDefaults())
                    .csrf(AbstractHttpConfigurer::disable)
                    .exceptionHandling(exceptionHandling ->
                            exceptionHandling
                                    .accessDeniedHandler(accessDeniedHandler)
                    )
                    .authorizeHttpRequests(authorizeRequests ->
                            authorizeRequests
                                    // allow all preflight requests
                                    .requestMatchers(HttpMethod.OPTIONS).permitAll()
                                    // allow all public endpoints
                                    .requestMatchers("/","/api/health","/api/authorities").permitAll()
                                    // account creation
                                    .requestMatchers(HttpMethod.POST, "/api/accounts").permitAll()
                                    // email verification
                                    .requestMatchers(HttpMethod.GET, "/api/accounts/verify-email").permitAll()
                                    .requestMatchers(HttpMethod.POST, "/api/accounts/verify-email/resend").permitAll()
                                    // password resetting
                                    .requestMatchers(HttpMethod.POST, "/api/accounts/reset-password","/api/accounts/reset-password/resend").permitAll()
                                    .requestMatchers(HttpMethod.GET, "/api/accounts/reset-password").permitAll()
                                    // others require authentication
                                    .anyRequest().authenticated()
                    );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}