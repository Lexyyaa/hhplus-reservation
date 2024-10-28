package com.hhplus.reservation.infra.queue;

import com.hhplus.reservation.domain.queue.WaitingQueue;
import com.hhplus.reservation.domain.queue.WaitingQueueStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface JPAWaitingQueueRepository extends JpaRepository<WaitingQueue, Long> {

	@Query("SELECT wq FROM WaitingQueue wq WHERE wq.userId = :userId AND wq.status = :status")
	Optional<WaitingQueue> findWaitingQueue(@Param("userId") Long userId, @Param("status") WaitingQueueStatus status);

	@Query("SELECT wq FROM WaitingQueue wq WHERE wq.userId = :userId AND wq.token = :token AND wq.status = 'WAITING'")
	Optional<WaitingQueue> findWaitingQueue(@Param("userId") Long userId, @Param("token") String token);

	@Query("SELECT COUNT(wq) + 1 FROM WaitingQueue wq " +
			"WHERE wq.createdAt < :createdAt AND wq.status = 'WAITING' ")
	Long findMyWaitNum(@Param("createdAt") LocalDateTime createdAt);

	@Query("SELECT wq FROM WaitingQueue wq WHERE wq.token = :token AND wq.status = 'PROCESS' AND (wq.expiredAt IS NOT NULL OR wq.deletedAt IS NOT NULL)")
	Optional<WaitingQueue> findInProcessQueue(@Param("token") String token);

	@Query("SELECT COUNT(wq) FROM WaitingQueue wq WHERE wq.status = 'PROCESS'")
	Long countProgressTokens();

	@Query("SELECT wq FROM WaitingQueue wq WHERE wq.status = 'WAITING' ORDER BY wq.createdAt ASC LIMIT 5")
	List<WaitingQueue> findNextTokens();

	@Modifying
	@Query("UPDATE WaitingQueue wq SET wq.status = :updateStatus WHERE wq.expiredAt < :currTime")
	void updateExpire(@Param("updateStatus") WaitingQueueStatus updateStatus,
			@Param("currTime") LocalDateTime currTime);

	@Modifying
	@Query("UPDATE WaitingQueue wq SET wq.status = 'PROCESS', wq.processedAt = :processedAt, wq.expiredAt = :expiredAt WHERE wq.id IN :queueList")
	void updateProcessTokens(List<Long> queueList, LocalDateTime processedAt, LocalDateTime expiredAt);

	@Modifying
	@Query("UPDATE WaitingQueue wq SET wq.deletedAt = :currTime WHERE wq.token = :token ")
	void updateTokenDone(@Param("token") String token, LocalDateTime currTime);

}
