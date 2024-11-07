import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    stages: [
        { duration: '1s', target: 1000 },
        { duration: '2s', target: 1000 },
        { duration: '1s', target: 0 }
    ],
    thresholds: {
        http_req_duration: ['p(99)<1000'],
        http_req_failed: ['rate<0.01']
    },
    summaryTrendStats: ['avg', 'p(90)', 'p(95)', 'p(99)', 'max']
};

// 고정된 콘서트 ID URL
const availableScheduleUrl = 'http://localhost:8080/api/concert/avaliable/2';

export default function () {
    // Authorization 헤더 없이 GET 요청 전송
    let response = http.get(availableScheduleUrl);

    // 응답 상태와 응답 시간 체크
    check(response, {
        'status is 200': (r) => r.status === 200,
        'response time < 1000ms': (r) => r.timings.duration < 1000,
    });

    sleep(1);
}