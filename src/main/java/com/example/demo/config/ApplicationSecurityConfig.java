package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig {

    private final SecurityContextExplicitSaveFilter explicitSaveFilter;

    public ApplicationSecurityConfig(SecurityContextExplicitSaveFilter explicitSaveFilter) {
        this.explicitSaveFilter = explicitSaveFilter;
    }

    @Bean
    @Order(-1)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
        //We now need to explicitly save the security context
        //https://docs.spring.io/spring-security/reference/servlet/authentication/persistence.html#securitycontextholderfilter

        http.securityContext(securityContextConfigurer -> securityContextConfigurer.requireExplicitSave(true))
        // uncomment below to make things work
//        .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
        ;

        // Use only the handle() method of XorCsrfTokenRequestAttributeHandler and the
        // default implementation of resolveCsrfTokenValue() from CsrfTokenRequestHandler
        // @see https://docs.spring.io/spring-security/reference/5.8/migration/servlet/exploits.html#_i_am_using_angularjs_or_another_javascript_framework
        CsrfTokenRequestHandler requestHandler = new XorCsrfTokenRequestAttributeHandler()::handle;
        http.csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .csrfTokenRequestHandler(requestHandler));

        http.addFilterAfter(explicitSaveFilter, CsrfFilter.class); // just place the filter somewhere for the PoC. But typically after an custom authentication filter

        return http.build();
    }
}
