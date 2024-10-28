package com.hhplus.reservation.domain.queue;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public enum WaitingQueueStatus {
	WAITING, PROCESS, EXPIRED
}
