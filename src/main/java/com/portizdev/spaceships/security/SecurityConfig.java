package com.portizdev.spaceships.security;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${api.key}")
    private String apiKey;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) 
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/**").permitAll() 
                .anyRequest().permitAll()
            )
            .addFilterBefore(new ApiKeyFilter(apiKey), UsernamePasswordAuthenticationFilter.class);  

        return http.build();
    }

    private static class ApiKeyFilter extends OncePerRequestFilter {
        private final String apiKey;

        public ApiKeyFilter(String apiKey) {
            this.apiKey = apiKey;
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {
            String requestApiKey = request.getHeader("x-api-key");

            if (apiKey.equals(requestApiKey)) {
                filterChain.doFilter(request, response); 
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  
            }
        }
    }
}
