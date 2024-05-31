package com.example.test.global.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
                .formLogin(
                        formLogin -> formLogin
                                .loginPage("/user/login")
                                .defaultSuccessUrl("/")
                )
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/oauth-login/admin").hasRole("ADMIN")
                        .requestMatchers("/oauth-login/info").authenticated()
                        .anyRequest().permitAll()
                )
                .formLogin((auth) -> auth.loginPage("/user/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/")
                        .failureUrl("/user/login")
                        .permitAll())
                .oauth2Login((auth) -> auth.loginPage("/user/login")
                        .defaultSuccessUrl("/")
                        .failureUrl("/user/login")
                        .permitAll())
                .logout((auth) -> auth
                        .logoutUrl("/logout"))
                .csrf((auth) -> auth.disable())
                .logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                                .logoutSuccessUrl("/")
                                .invalidateHttpSession(true));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
