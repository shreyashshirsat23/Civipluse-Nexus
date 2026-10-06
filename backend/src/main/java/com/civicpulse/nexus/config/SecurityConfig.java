package com.civicpulse.nexus.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            // Use the CorsConfigurationSource bean automatically
            .cors(Customizer.withDefaults())

            .authorizeHttpRequests(auth -> auth

                .requestMatchers(
                    "/actuator/health",
                    "/api/public/**"
                ).permitAll()

                .requestMatchers(HttpMethod.GET, "/api/dashboard/**")
                .hasAnyRole(
                    "ADMIN",
                    "COMMISSIONER",
                    "OFFICER"
                )

                .requestMatchers("/api/audit/**")
                .hasAnyRole(
                    "ADMIN",
                    "COMMISSIONER"
                )

                .anyRequest()
                .authenticated()
            )

            .oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwt ->
                    jwt.jwtAuthenticationConverter(
                        jwtAuthenticationConverter()
                    )
                )
            );

        return http.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {

        JwtAuthenticationConverter converter =
            new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {

            Map<String, Object> access =
                jwt.getClaimAsMap("realm_access");

            if (access == null) {
                return List.<GrantedAuthority>of();
            }

            Object rolesObject = access.get("roles");

            if (!(rolesObject instanceof Collection<?> roles)) {
                return List.<GrantedAuthority>of();
            }

            return roles.stream()
                .map(Object::toString)
                .map(role ->
                    (GrantedAuthority) new SimpleGrantedAuthority(
                        "ROLE_" +
                        role.toUpperCase(Locale.ROOT)
                    )
                )
                .toList();
        });

        return converter;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(
            @Value("${civicpulse.cors-origins}") String origins) {

        CorsConfiguration configuration =
            new CorsConfiguration();

        configuration.setAllowedOrigins(
            Arrays.stream(origins.split(","))
                .map(String::trim)
                .filter(origin -> !origin.isBlank())
                .toList()
        );

        configuration.setAllowedMethods(
            List.of(
                "GET",
                "POST",
                "PUT",
                "PATCH",
                "DELETE",
                "OPTIONS"
            )
        );

        configuration.setAllowedHeaders(
            List.of("*")
        );

        configuration.setExposedHeaders(
            List.of("Location")
        );

        UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
            "/**",
            configuration
        );

        return source;
    }
}