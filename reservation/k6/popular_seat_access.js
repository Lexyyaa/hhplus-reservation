import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    scenarios: {
        load_test: {
            executor: 'ramping-vus',
            startVUs: 0,
            stages: [
                { duration: '1s', target: 1 },
                { duration: '2s', target: 5 },
                { duration: '1s', target: 0 },
            ],
        },
        endurance_test: {
            executor: 'constant-vus',
            vus: 10,
            duration: '4s',
        },
        stress_test: {
            executor: 'ramping-vus',
            startVUs: 0,
            stages: [
                { duration: '2s', target: 5 },
                { duration: '2s', target: 20 },
                { duration: '2s', target: 0 },
            ],
        },
        peak_test: {
            executor: 'constant-vus',
            vus: 10,
            duration: '2s',
        },
    },
    thresholds: {
        http_req_duration: ['p(99)<1000'],
        http_req_failed: ['rate<0.01'],
        http_req_body_size: ['p(95)<1000'],
        http_reqs: ['count>100'],
    },
    summaryTrendStats: ['avg', 'p(90)', 'p(95)', 'p(99)', 'max'],
};

export default function () {
    const userId = Math.floor(Math.random() * 10000) + 1;
    const scheduleId = 2;
    const seats = [Math.floor(Math.random() * 5) + 1];

    const payload = JSON.stringify({
        userId: userId,
        seats: seats
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    const res = http.post(`http://localhost:28080/api/reserve/${scheduleId}/seat/${userId}`, payload, params);
    check(res, { 'status is 200': (r) => r.status === 200 });
    sleep(1);
}
