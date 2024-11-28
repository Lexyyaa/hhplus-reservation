import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    scenarios: {
        load_test: {
            executor: 'ramping-vus',
            startVUs: 0,
            stages: [
                { duration: '10s', target: 30 },
                { duration: '15s', target: 100 },
                { duration: '15s', target: 300 },
                { duration: '10s', target: 0 },
            ],
        },
        endurance_test: {
            executor: 'constant-vus',
            vus: 1000,
            duration: '30s',
        },
        stress_test: {
            executor: 'ramping-vus',
            startVUs: 0,
            stages: [
                { duration: '10s', target: 30 },
                { duration: '15s', target: 200 },
                { duration: '15s', target: 500 },
                { duration: '5s', target: 0 },
            ],
        },
        peak_test: {
            executor: 'constant-vus',
            vus: 1000,
            duration: '10s',
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
    const concertId = 2;
    const userId = Math.floor(Math.random() * 10000) + 1;

    const scheduleRes = http.get(`http://localhost:28080/api/concert/avaliable/${concertId}`);
    check(scheduleRes, { 'Schedule status is 200': (r) => r.status === 200 });

    let scheduleId;
    try {
        const scheduleData = JSON.parse(scheduleRes.body);
        if (Array.isArray(scheduleData) && scheduleData.length > 0) {
            scheduleId = scheduleData[0].id;
        }
    } catch (e) {
        console.error('Failed to parse schedule response:', e);
    }

    if (scheduleId) {
        const seatRes = http.get(`http://localhost:28080/api/concert/avaliable/seat/${scheduleId}`);
        check(seatRes, { 'Seat status is 200': (r) => r.status === 200 });

        const reservationRes = http.post(`http://localhost:28080/api/reserve/${scheduleId}/seat/${userId}`);
        check(reservationRes, { 'Reservation status is 200': (r) => r.status === 200 });

        let reservationId;
        try {
            const reservationData = JSON.parse(reservationRes.body);
            if (reservationData && reservationData.id) {
                reservationId = reservationData.id;
            }
        } catch (e) {
            console.error('Failed to parse reservation response:', e);
        }

        if (reservationId) {
            const paymentRes = http.post(`http://localhost:28080/api/pay/${reservationId}/${userId}`);
            check(paymentRes, { 'Payment status is 200': (r) => r.status === 200 });
        }
    }

    sleep(1);
}