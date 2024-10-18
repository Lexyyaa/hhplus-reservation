package com.hhplus.reservation.domain.concert;

import com.hhplus.reservation.application.dto.ConcertInfo;
import com.hhplus.reservation.application.dto.ConcertScheduleInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConcertServiceTest {

    @Mock
    private ConcertRepository concertRepository;

    @InjectMocks
    private ConcertService concertService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("콘서트 정보를 성공적으로 조회한다.")
    void 콘서트_정보를_성공적으로_조회한다() {
        Long concertId = 1L;
        Concert concert = Concert.builder().id(concertId).title("Test Concert").build();

        when(concertRepository.getConcert(concertId)).thenReturn(concert);

        ConcertInfo result = concertService.getConcert(concertId);

        assertNotNull(result);
        assertEquals("Test Concert", result.getTitle());
    }

    @Test
    @DisplayName("콘서트 조회에 실패한다 - 콘서트 정보가 존재하지 않을 때")
    void 콘서트_조회에_실패한다_콘서트_정보가_존재하지_않을_때() {
        Long concertId = 1L;

        when(concertRepository.getConcert(concertId))
                .thenThrow(new RuntimeException("콘서트 정보가 존재하지 않습니다."));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> concertService.getConcert(concertId)
        );

        assertEquals("콘서트 정보가 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("예약 가능한 날짜를 성공적으로 조회한다.")
    void 예약_가능한_날짜를_성공적으로_조회한다() {
        Long concertId = 1L;
        List<ConcertSchedule> schedules = List.of(
                ConcertSchedule.builder().id(1L).concertId(concertId).performDate(LocalDate.now()).build()
        );

        when(concertRepository.getSchedules(concertId)).thenReturn(schedules);

        List<ConcertScheduleInfo> result = concertService.getSchedules(concertId);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("예약 가능한 날짜 조회에 실패한다 - 스케줄이 존재하지 않을 때")
    void 예약_가능한_날짜_조회에_실패한다_스케줄이_존재하지_않을_때() {
        Long concertId = 1L;

        // Mock 설정: 빈 리스트 반환
        when(concertRepository.getSchedules(concertId)).thenReturn(List.of());

        // 예외 발생 여부 확인
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> concertService.getSchedules(concertId)
        );

        // 예외 메시지 검증
        assertEquals("예약 가능한 날짜가 존재하지 않습니다.", exception.getMessage());
    }
}