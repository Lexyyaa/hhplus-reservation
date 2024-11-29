import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    scenarios: {
        load_test: {
            executor: 'ramping-vus',
            startVUs: 0,
            stages: [
                { duration: '30s', target: 100 },
                { duration: '30s', target: 200 },
                { duration: '30s', target: 300 },
                { duration: '30s', target: 0 },
            ],
        },
        endurance_test: {
            executor: 'constant-vus',
            vus: 300,
            duration: '2m',
        },
        stress_test: {
            executor: 'ramping-vus',
            startVUs: 0,
            stages: [
                { duration: '30s', target: 100 },
                { duration: '30s', target: 250 },
                { duration: '30s', target: 300 },
                { duration: '30s', target: 0 },
            ],
        },
        peak_test: {
            executor: 'constant-vus',
            vus: 500,
            duration: '1m',
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
    const res = http.post(`http://localhost:28080/api/queue/token/${userId}`);
    check(res, { 'status is 200': (r) => r.status === 200 });
    sleep(1);
}
