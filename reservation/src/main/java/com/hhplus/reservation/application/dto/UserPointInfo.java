package com.hhplus.reservation.application.dto;

import com.hhplus.reservation.interfaces.dto.point.UserPointResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPointInfo {
    long userId;
    long point;

    public static UserPointResponse convert(UserPointInfo userPoint){
        return UserPointResponse.builder()
                .userId(userPoint.getUserId())
                .point(userPoint.getPoint())
                .build();
    }
}
