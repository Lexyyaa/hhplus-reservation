package com.hhplus.reservation.interfaces.api;

import com.hhplus.reservation.domain.token.TokenStatus;
import com.hhplus.reservation.interfaces.dto.token.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/queue")
public class TokenController {

    // Mock API에 사용할 객체
    public static Queue<String> waitingQueue =  new LinkedList<String>() ;
    static{
        waitingQueue.add("MTA6ZGM0ZDU0NDYtNDg5NC00NzgzLWFmMmYtODZiYjUwNDgxOTdh");
    }

    // 유저 대기열 토큰 생성 API
    @PostMapping("/token/{userId}")
      public ResponseEntity<TokenResponse> makeQueue(@PathVariable("userId") Long userId, @RequestHeader("Authorization") String queueToken) {
        TokenResponse response = new TokenResponse();

        if(queueToken == null || !waitingQueue.contains(queueToken)){

            // 토큰 생성
            String newToken = makeToken(20L);
            waitingQueue.add(newToken);

            response.setUserId(20L);
            response.setToken(newToken);
            response.setWaitNum(10);
            response.setStatus(TokenStatus.WATING);
            response.setCreatedAt(LocalDateTime.of(2024, 10, 10, 10, 10, 10));

        }else {
            System.out.println("else");
            response.setUserId(10L);
            response.setToken(queueToken);
            response.setWaitNum(5);
            response.setStatus(TokenStatus.WATING);
            response.setCreatedAt(LocalDateTime.of(2024, 10, 10, 10, 10, 10));
        }
        return ResponseEntity.ok().body(response);
      }

      // UUID를 활용하여 토큰생성
      private static String makeToken(Long userId){
          UUID uuid = UUID.nameUUIDFromBytes(userId.toString().getBytes());
          String concatStr = userId + "__" + uuid.toString();
          return Base64.getEncoder().encodeToString(concatStr.getBytes());
      }










}
