package com.taxshelf.taxshelf_api.filters;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoggingFilter extends OncePerRequestFilter{

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
                System.out.println("Request:" + request.getRequestURL());
                filterChain.doFilter(request, response);
                System.out.println("Response:" + response.getStatus());
                //t hrow new UnsupportedOperationException("Unimplemented method 'doFilterInternal'");
    }

}
