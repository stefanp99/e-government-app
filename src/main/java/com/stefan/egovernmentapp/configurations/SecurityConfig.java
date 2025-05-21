package com.stefan.egovernmentapp.configurations;

import com.stefan.egovernmentapp.security_filters.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.stefan.egovernmentapp.models.Role.ADMIN;
import static com.stefan.egovernmentapp.models.Role.EMPLOYEE;
import static com.stefan.egovernmentapp.models.Role.RESIDENT;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/register-resident", "/auth/login").permitAll()
                        .requestMatchers(POST, "/auth/register").hasAnyRole(ADMIN.toString(), EMPLOYEE.toString())
                        .requestMatchers(PUT, "/auth/2fa").hasAnyRole(ADMIN.toString(), RESIDENT.toString(), EMPLOYEE.toString())
                        .requestMatchers(DELETE, "/auth/2fa").hasAnyRole(ADMIN.toString(), RESIDENT.toString(), EMPLOYEE.toString())
                        .requestMatchers(GET, "/pending-residents-requests").hasAnyRole(ADMIN.toString(), EMPLOYEE.toString())
                        .requestMatchers(PATCH, "/pending-residents-requests/**").hasAnyRole(ADMIN.toString(), EMPLOYEE.toString())
                        .requestMatchers(POST, "/pending-residents-requests").permitAll()
                        .requestMatchers(POST, "/complaints").hasAnyRole(ADMIN.toString(), RESIDENT.toString())
                        .requestMatchers(PATCH, "/complaints/**").hasAnyRole(ADMIN.toString(), EMPLOYEE.toString())
                        .requestMatchers(GET, "/complaints/resident").hasAnyRole(ADMIN.toString(), RESIDENT.toString())
                        .requestMatchers(GET, "/complaints/employee").hasAnyRole(ADMIN.toString(), EMPLOYEE.toString())
                        .requestMatchers(POST, "/polls").hasAnyRole(ADMIN.toString(), EMPLOYEE.toString())
                        .requestMatchers(PATCH, "/polls/**").hasAnyRole(ADMIN.toString(), EMPLOYEE.toString())
                        .requestMatchers(GET, "/polls/**").hasAnyRole(ADMIN.toString(), RESIDENT.toString(), EMPLOYEE.toString())
                        .requestMatchers(POST, "/votes").hasAnyRole(ADMIN.toString(), RESIDENT.toString())
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}