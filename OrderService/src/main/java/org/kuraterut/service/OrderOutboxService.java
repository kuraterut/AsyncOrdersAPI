package org.kuraterut.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kuraterut.order.OrderEvent;
import org.kuraterut.model.entity.OrderOutbox;
import org.kuraterut.repository.OrderOutboxRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderOutboxService {
    private final OrderOutboxRepository outboxRepository;
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @Value("${spring.kafka.producer.topic}")
    private String topic;



    @Transactional
    @Scheduled(fixedDelayString = "${schedule.delay.sending.outbox}")
    public void processOutboxMessages() {
        List<OrderOutbox> messages = outboxRepository.findByProcessedFalseOrderByCreatedAtAsc();
        log.info("Processing outbox messages: {}", messages.size());

        for (OrderOutbox message : messages) {
            try {
                OrderEvent event = new OrderEvent();
                event.setOrderId(message.getOrderId());
                event.setUserId(message.getUserId());
                event.setAmount(message.getAmount());

                kafkaTemplate.send(topic, event).get();

                outboxRepository.markAsProcessed(message.getId());
            } catch (Exception e) {
                log.error("Failed to process outbox message with id: {}", message.getId(), e);
            }
        }
    }
}