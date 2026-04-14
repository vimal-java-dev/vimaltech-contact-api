package com.vimaltech.contactapi.config;

// import org.jspecify.annotations.NonNull;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.CorsRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
 * =========================================================================
 * DISABLED: CORS is now handled globally at the Nginx reverse-proxy level.
 * =========================================================================
 * If we leave this enabled, Spring Boot and Nginx will BOTH inject
 * Access-Control-Allow-Origin headers, causing a browser security failure.
 */

// @Configuration
// public class CorsConfig {
//
//     @Bean
//     public WebMvcConfigurer corsConfigurer() {
//         return new WebMvcConfigurer() {
//             @Override
//             public void addCorsMappings(@NonNull CorsRegistry registry) {
//                 registry.addMapping("/**")
//                         .allowedOrigins(
//                                 "http://localhost:3000",
//                                 "https://vimaltech.dev")
//                         .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                         .allowedHeaders("*");
//             }
//         };
//     }
// }