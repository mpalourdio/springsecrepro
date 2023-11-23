package com.example.demo.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component

public class SecurityContextExplicitSaveFilter extends OncePerRequestFilter {

    private final SecurityContextRepository repository = new HttpSessionSecurityContextRepository();

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest req,
            @NonNull HttpServletResponse res,
            FilterChain chain
    ) throws ServletException, IOException {
        try {
            chain.doFilter(req, res);
        } finally {
            var context = SecurityContextHolder.getContext();
            SecurityContextHolder.clearContext();
            repository.saveContext(context, req, res);
            logger.debug("saved context to complete filter");
        }
    }
}
