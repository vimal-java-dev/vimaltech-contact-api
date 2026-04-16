package com.vimaltech.contactapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable()) // API use-case
                .authorizeHttpRequests(auth -> auth

                        // ✅ Public endpoints
                        .requestMatchers(HttpMethod.POST, "/api/v1/contacts").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/contacts/options").permitAll()

                        // 🔒 Admin endpoint
                        .requestMatchers(HttpMethod.GET, "/api/v1/contacts").authenticated()

                        // Everything else
                        .anyRequest().permitAll()
                )
                // ✅ NEW WAY (Spring Security 6+)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        String password = System.getenv("ADMIN_PASSWORD");

        UserDetails user = User
                .withUsername("admin")
                .password("{noop}" + password)
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user);
    }
}