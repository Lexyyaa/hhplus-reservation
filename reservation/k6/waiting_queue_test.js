import http from 'k6/http';
import { check, sleep } from 'k6';
import { SharedArray } from 'k6/data';

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

const userTokenPairs = new SharedArray("user-token-pairs", function() {
    return [
        { userId: 1, token: 'MV9fYzRjYTQyMzgtYTBiOS0zMzgyLThkY2MtNTA5YTZmNzU4NDli' },
        { userId: 2, token: 'Ml9fYzgxZTcyOGQtOWQ0Yy0zZjYzLWFmMDYtN2Y4OWNjMTQ4NjJj' },
        { userId: 3, token: 'M19fZWNjYmM4N2UtNGI1Yy0zMmZlLWE4MzAtOGZkOWYyYTdiYWYz' },
        { userId: 4, token: 'NF9fYTg3ZmY2NzktYTJmMy0zNzFkLTkxODEtYTY3Yjc1NDIxMjJj' },
        { userId: 5, token: 'NV9fZTRkYTNiN2YtYmJjZS0zMzQ1LTk3NzctMmIwNjc0YTMxOGQ1' },
        { userId: 6, token: 'Nl9fMTY3OTA5MWMtNWE4OC0zZmFmLWFmYjUtZTYwODdlYjFiMmRj' },
    ];
});

const issueTokenUrl = 'http://localhost:8080/api/queue/token';
const pollingUrl = 'http://localhost:8080/api/queue/polling';

export default function () {
    // 랜덤하게 user정보를 입력
    const userTokenPair = userTokenPairs[Math.floor(Math.random() * userTokenPairs.length)];
    const userId = userTokenPair.userId;
    const queueToken = userTokenPair.token;

    // 토큰 발급 요청
    let issueTokenResponse = http.post(`${issueTokenUrl}/${userId}`);
    check(issueTokenResponse, {
        'issueToken status is 200': (r) => r.status === 200,
        'issueToken response time < 1000ms': (r) => r.timings.duration < 1000,
    });

    // 폴링 요청
    let headers = { Authorization: queueToken };
    let pollingResponse = http.get(`${pollingUrl}/${userId}`, { headers: headers });
    check(pollingResponse, {
        'polling status is 200': (r) => r.status === 200,
        'polling response time < 1000ms': (r) => r.timings.duration < 1000,
    });

    sleep(1);
}