package h2go.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import h2go.service.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    log.info("Configuring AuthenticationManager");
    AuthenticationManager authManager = authenticationConfiguration.getAuthenticationManager();
    log.info("AuthenticationManager configured successfully");
    return authManager;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, @Lazy AuthTokenFilter authTokenFilter,
      @Lazy AuthEntryPoint authEntryPoint) throws Exception {

    log.info("Configuring SecurityFilterChain");
    
    http
        .csrf(AbstractHttpConfigurer::disable)
        .cors(AbstractHttpConfigurer::disable)
        .exceptionHandling(e -> e.authenticationEntryPoint(authEntryPoint))
        .sessionManagement(
            s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            request -> request.requestMatchers("/api/auth/**", "/actuator/**").permitAll()
                .anyRequest().authenticated());

    http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
    
    log.info("SecurityFilterChain configured successfully");
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}