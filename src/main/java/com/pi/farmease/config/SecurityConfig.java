
package com.pi.farmease.config;


import com.pi.farmease.entities.enumerations.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor

public class SecurityConfig  implements WebMvcConfigurer {

    private final JwtAuthenticationFilter jwtAuthFilter ;
    private final AuthenticationProvider authenticationProvider ;



    private final String[] whitelist = {"/api/v1/auth/**", "/images/**"};
    @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

      http.csrf(AbstractHttpConfigurer::disable)
              .cors(cors -> {
                  cors.configurationSource(corsConfigurationSource());
              })
              .authorizeHttpRequests(req ->
              req.requestMatchers(whitelist)
              .permitAll()
                      .requestMatchers("/api/v1/admin/**").hasAnyAuthority(Role.ADMIN.toString())
              .anyRequest()
              .authenticated()
              )
              .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
              .authenticationProvider(authenticationProvider)
              .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) ;

               return http.build();
  }




    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:4200");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new
                UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;

    }


}
