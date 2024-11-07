-- Concert
INSERT INTO concert (id, title, created_at, updated_at, deleted_at) VALUES    (1, '싸이흠뻑쇼(서울)', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO concert (id, title, created_at, updated_at, deleted_at) VALUES    (2, '싸이흠뻑쇼(부산)', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

-- ConcertSchedule
INSERT INTO concert_schedule (id, concert_id, perform_date, total_seat, available_seat_num, available_status, created_at, updated_at, deleted_at) VALUES    (1, 1, '2024-11-11', 50, 50, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO concert_schedule (id, concert_id, perform_date, total_seat, available_seat_num, available_status, created_at, updated_at, deleted_at) VALUES    (2, 1, '2024-11-12', 50, 50, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO concert_schedule (id, concert_id, perform_date, total_seat, available_seat_num, available_status, created_at, updated_at, deleted_at) VALUES    (3, 2, '2024-11-13', 50, 50, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO concert_schedule (id, concert_id, perform_date, total_seat, available_seat_num, available_status, created_at, updated_at, deleted_at) VALUES    (4, 2, '2024-11-14', 50, 0, 'UNAVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO concert_schedule (id, concert_id, perform_date, total_seat, available_seat_num, available_status, created_at, updated_at, deleted_at) VALUES    (5, 1, '2024-11-15', 50, 50, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO concert_schedule (id, concert_id, perform_date, total_seat, available_seat_num, available_status, created_at, updated_at, deleted_at) VALUES    (6, 2, '2024-11-16', 50, 0, 'UNAVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO concert_schedule (id, concert_id, perform_date, total_seat, available_seat_num, available_status, created_at, updated_at, deleted_at) VALUES    (7, 2, '2024-11-16', 50, 50, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO concert_schedule (id, concert_id, perform_date, total_seat, available_seat_num, available_status, created_at, updated_at, deleted_at) VALUES    (8, 2, '2024-11-16', 50, 50, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO concert_schedule (id, concert_id, perform_date, total_seat, available_seat_num, available_status, created_at, updated_at, deleted_at) VALUES    (9, 2, '2024-11-16', 50, 50, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO concert_schedule (id, concert_id, perform_date, total_seat, available_seat_num, available_status, created_at, updated_at, deleted_at) VALUES    (10, 2, '2024-11-16', 50, 50, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO concert_schedule (id, concert_id, perform_date, total_seat, available_seat_num, available_status, created_at, updated_at, deleted_at) VALUES    (11, 2, '2024-11-16', 50, 50, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO concert_schedule (id, concert_id, perform_date, total_seat, available_seat_num, available_status, created_at, updated_at, deleted_at) VALUES    (12, 2, '2024-11-16', 50, 50, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO concert_schedule (id, concert_id, perform_date, total_seat, available_seat_num, available_status, created_at, updated_at, deleted_at) VALUES    (13, 2, '2024-11-16', 50, 50, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO concert_schedule (id, concert_id, perform_date, total_seat, available_seat_num, available_status, created_at, updated_at, deleted_at) VALUES    (14, 2, '2024-11-16', 50, 50, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO concert_schedule (id, concert_id, perform_date, total_seat, available_seat_num, available_status, created_at, updated_at, deleted_at) VALUES    (15, 2, '2024-11-16', 50, 50, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

-- ConcertSeat (concert_schedule_id = 1)
INSERT INTO concert_seat (id, concert_schedule_id, seat_num, seat_type, seat_price, seat_status, version, created_at, updated_at, deleted_at) VALUES    (1, 1, 'A1', 'S', 30000, 'AVAILABLE', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO concert_seat (id, concert_schedule_id, seat_num, seat_type, seat_price, seat_status, version, created_at, updated_at, deleted_at) VALUES    (2, 1, 'A2', 'S', 30000, 'AVAILABLE', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO concert_seat (id, concert_schedule_id, seat_num, seat_type, seat_price, seat_status, version, created_at, updated_at, deleted_at) VALUES    (3, 1, 'A3', 'R', 25000, 'AVAILABLE', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO concert_seat (id, concert_schedule_id, seat_num, seat_type, seat_price, seat_status, version, created_at, updated_at, deleted_at) VALUES    (4, 1, 'A4', 'R', 25000, 'AVAILABLE', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO concert_seat (id, concert_schedule_id, seat_num, seat_type, seat_price, seat_status, version, created_at, updated_at, deleted_at) VALUES    (5, 1, 'B1', 'A', 20000, 'AVAILABLE', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO concert_seat (id, concert_schedule_id, seat_num, seat_type, seat_price, seat_status, version, created_at, updated_at, deleted_at) VALUES    (6, 1, 'B2', 'A', 20000, 'AVAILABLE', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

-- ConcertSeat (concert_schedule_id = 2)
INSERT INTO concert_seat (id, concert_schedule_id, seat_num, seat_type, seat_price, seat_status, version, created_at, updated_at, deleted_at) VALUES    (21, 2, 'A1', 'S', 30000, 'AVAILABLE', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO concert_seat (id, concert_schedule_id, seat_num, seat_type, seat_price, seat_status, version, created_at, updated_at, deleted_at) VALUES    (22, 2, 'A2', 'S', 30000, 'UNAVAILABLE', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO concert_seat (id, concert_schedule_id, seat_num, seat_type, seat_price, seat_status, version, created_at, updated_at, deleted_at) VALUES    (23, 2, 'A3', 'R', 25000, 'AVAILABLE', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO concert_seat (id, concert_schedule_id, seat_num, seat_type, seat_price, seat_status, version, created_at, updated_at, deleted_at) VALUES    (24, 2, 'B1', 'A', 20000, 'AVAILABLE', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO concert_seat (id, concert_schedule_id, seat_num, seat_type, seat_price, seat_status, version, created_at, updated_at, deleted_at) VALUES    (25, 2, 'B2', 'B', 15000, 'UNAVAILABLE', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO concert_seat (id, concert_schedule_id, seat_num, seat_type, seat_price, seat_status, version, created_at, updated_at, deleted_at) VALUES    (26, 2, 'B3', 'B', 15000, 'AVAILABLE', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

-- UserPoint 더미 데이터 삽입
INSERT INTO users (id, name, point, created_at, updated_at, deleted_at) VALUES (1, '유저1', 40000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO users (id, name, point, created_at, updated_at, deleted_at) VALUES (2, '유저2', 55000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO users (id, name, point, created_at, updated_at, deleted_at) VALUES (3, '유저3', 55000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO users (id, name, point, created_at, updated_at, deleted_at) VALUES (4, '유저4', 50000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

-- 1번 유저의 예약: 5번과 6번 좌석
INSERT INTO reservation (id, user_id, concert_schedule_id, total_price, reserved, reserve_request_at, reserve_expired_at, reserved_at, created_at, updated_at, version)VALUES (1, 1, 1, 40000, 'TEMP_RESERVED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '5' MINUTE, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,1);

INSERT INTO reservation_seat (id, reservation_id, seat_id, created_at, updated_at)VALUES (1, 1, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO reservation_seat (id, reservation_id, seat_id, created_at, updated_at)VALUES (2, 1, 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 2번 유저의 예약: 1번과 2번 좌석
INSERT INTO reservation (id, user_id, concert_schedule_id, total_price, reserved, reserve_request_at, reserve_expired_at, reserved_at, created_at, updated_at, version)VALUES (2, 2, 1, 60000, 'TEMP_RESERVED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '5' MINUTE, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,1);

INSERT INTO reservation_seat (id, reservation_id, seat_id, created_at, updated_at)VALUES (3, 2, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO reservation_seat (id, reservation_id, seat_id, created_at, updated_at)VALUES (4, 2, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 3번 유저의 만료된 예약: 3번과 4번 좌석
INSERT INTO reservation (id, user_id, concert_schedule_id, total_price, reserved, reserve_request_at, reserve_expired_at, reserved_at, created_at, updated_at, version)VALUES (3, 3, 1, 25000, 'TEMP_RESERVED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '1' SECOND, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,1);

INSERT INTO reservation_seat (id, reservation_id, seat_id, created_at, updated_at)VALUES (5, 3, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO reservation_seat (id, reservation_id, seat_id, created_at, updated_at)VALUES (6, 3, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);