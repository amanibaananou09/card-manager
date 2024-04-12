package com.teknokote.cm.authentification.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

   private final JwtAuthConverter jwtAuthConverter;
   private final UsernamePasswordAuthenticationTokenConverter usernamePasswordAuthenticationTokenConverter;


   @Bean
   public SecurityFilterChain oauth2SecurityFilterChain(HttpSecurity http) throws Exception
   {
      http
              .csrf()
              .disable()
              .cors()
              .and()
              .authorizeHttpRequests()
              .requestMatchers( "/websocket","/login","/user/reset-password","/user/forgot-password","/websocketFrontEnd","/websocket-endpoint/**").permitAll()
              .anyRequest()
              .authenticated();

      http
              .oauth2ResourceServer()
              .jwt()
              .jwtAuthenticationConverter(jwtAuthConverter.andThen(usernamePasswordAuthenticationTokenConverter));

      http
              .sessionManagement()
              .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

      return http.build();
   }

   @Bean
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }
}
