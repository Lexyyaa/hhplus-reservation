package com.hhplus.reservation.interfaces.api.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class TokenChkFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String token = httpRequest.getHeader("Authorization");
        String path = httpRequest.getRequestURI();

        // 이따 여기 지우자
        if (httpRequest.getRequestURI().startsWith("/h2-console")) {
            chain.doFilter(request, response); 
            return;
        }

        if (path.contains("/api/queue/token")) { 
            chain.doFilter(request, response);
            return;
        } 

        if (token == null || token.isEmpty()) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            log.warn("허가되지 않은 접근입니다."); // Exception 처리?
        }

        chain.doFilter(request, response);
    }
}