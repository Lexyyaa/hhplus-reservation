package com.hhplus.reservation.domain.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageOutboxService {

    private final MessageOutboxRepository messageOutboxRepository;

    /**
     * 아웃박스메시지를 저장한다.
     */
    public MessageOutbox save(MessageOutbox messageOutbox) {
        return messageOutboxRepository.save(messageOutbox);
    }

    /**
     * 메시지를 발행상태로 변경한다.
     */
    @Transactional
    public void chkPublished(Long messageKey,String type){
        MessageOutbox messageOutbox = messageOutboxRepository.findByEventIdAndEventType(messageKey,type);
        messageOutbox.published();
        messageOutboxRepository.save(messageOutbox);
    }

    /**
     * 재실행할 메시지 목록을 가져온다.
     */
    public List<MessageOutbox> findRetryMessages(){
        return messageOutboxRepository.findRetryMessages();
    }

    public MessageOutbox countUpRetry(MessageOutbox messageOutbox){
        messageOutbox.countUp();
        return messageOutboxRepository.save(messageOutbox);
    }
}
