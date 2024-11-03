package com.hhplus.reservation.interfaces.api.interceptor;

import com.hhplus.reservation.domain.queue.WaitingQueueRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenInterceptor implements HandlerInterceptor {

    private final WaitingQueueRepository queueRepository;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String queueToken = request.getHeader("Authorization");

        boolean isValidToken = queueRepository.validateToken(queueToken);

        if(!isValidToken){
            log.warn("검증되지않은 토큰입니다.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        return isValidToken;
    }
}