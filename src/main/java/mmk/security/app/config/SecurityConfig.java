package mmk.security.app.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import lombok.RequiredArgsConstructor;
import mmk.security.app.comp.JwtAuthenticationFilter;
import mmk.security.app.enums.Authority;
import mmk.security.app.service.UserJwtService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final JwtAuthenticationFilter jwtAuthFilter;
	private final UserJwtService userService;
	private final PasswordEncoder passwordEncoder;
	private final LogoutHandler logoutHandler;
	
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
        	.authorizeHttpRequests(req ->  
        		req.requestMatchers("/auth/**")
        		.permitAll()
        		.requestMatchers("/adm/**").hasAnyAuthority(Authority.ROLE_ADMIN.getAuthority())
        		.requestMatchers("/users/**").hasAnyAuthority(Authority.ROLE_ADMIN.getAuthority(), Authority.ROLE_USER.getAuthority())
        		.anyRequest()
                .authenticated())
        	.sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
        	.authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        
        http.logout(logout ->
        		logout.logoutUrl("/auth/logout")
        			.addLogoutHandler(logoutHandler)
        			.logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()));
        
        return http.build();
    }
    
    @Bean
    AuthenticationProvider authenticationProvider() {
      DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
      authProvider.setUserDetailsService(userService);
      authProvider.setPasswordEncoder(passwordEncoder);
      return authProvider;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
      return config.getAuthenticationManager();
    }

}
