package org.kuraterut.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kuraterut.model.entity.Account;
import org.kuraterut.model.entity.PaymentInbox;
import org.kuraterut.model.entity.PaymentOutbox;
import org.kuraterut.order.OrderEvent;
import org.kuraterut.order.PaymentEventResult;
import org.kuraterut.order.PaymentResult;
import org.kuraterut.repository.AccountRepository;
import org.kuraterut.repository.PaymentInboxRepository;
import org.kuraterut.repository.PaymentOutboxRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final PaymentInboxRepository paymentInboxRepository;
    private final PaymentOutboxRepository paymentOutboxRepository;
    private final AccountRepository accountRepository;
    private final KafkaTemplate<String, PaymentResult> kafkaTemplate;

    @Value("${spring.kafka.producer.topic}")
    private String paymentResultTopic;


    @KafkaListener(topics = "${spring.kafka.consumer.topic}", groupId = "${spring.kafka.consumer.group-id}")
    @Transactional
    public void listenOrderEvent(String message, Acknowledgment acknowledgment) throws JsonProcessingException {
        try{
            ObjectMapper mapper = new ObjectMapper();
            OrderEvent orderEvent = mapper.readValue(message, OrderEvent.class);
            PaymentInbox paymentInbox = new PaymentInbox();
            paymentInbox.setOrderId(orderEvent.getOrderId());
            paymentInbox.setUserId(orderEvent.getUserId());
            paymentInbox.setAmount(orderEvent.getAmount());
            paymentInbox.setCreatedAt(LocalDateTime.now());
            paymentInbox.setProcessed(false);
            paymentInboxRepository.save(paymentInbox);
            acknowledgment.acknowledge();
        } catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }
    }

    @Transactional
    @Scheduled(fixedDelayString = "${schedule.delay.payment.process}")
    public void executePaymentProcess() {
        List<PaymentInbox> paymentInboxes = paymentInboxRepository.findTop100ByProcessedFalseOrderByCreatedAtAsc();
        for(PaymentInbox paymentInbox : paymentInboxes){
            BigDecimal amount = paymentInbox.getAmount();
            Long userId = paymentInbox.getUserId();

            PaymentOutbox paymentOutbox = new PaymentOutbox();
            paymentOutbox.setOrderId(paymentInbox.getOrderId());
            paymentOutbox.setUserId(paymentInbox.getUserId());
            paymentOutbox.setAmount(amount);
            paymentOutbox.setPublished(false);

            if(!accountRepository.existsByUserId(userId)){
                paymentOutbox.setResult(PaymentEventResult.FAILED);
                paymentOutboxRepository.save(paymentOutbox);
                paymentInboxRepository.markAsProcessed(paymentInbox.getId());
                return;
            }

            int updatedRows = accountRepository.removeMoneyByUserId(userId, amount);

            if (updatedRows > 0) {
                paymentOutbox.setResult(PaymentEventResult.SUCCESS);
            } else {
                paymentOutbox.setResult(PaymentEventResult.FAILED);
            }
            paymentOutboxRepository.save(paymentOutbox);
            paymentInboxRepository.markAsProcessed(paymentInbox.getId());
        }
    }

    @Transactional
    @Scheduled(fixedDelayString = "${schedule.delay.payment.publish-results}")
    public void publishPaymentResult(){
        List<PaymentOutbox> paymentOutboxes = paymentOutboxRepository.findTop100ByPublishedFalse();
        for(PaymentOutbox paymentOutbox : paymentOutboxes){
            try {
                PaymentResult paymentResult = new PaymentResult();
                paymentResult.setOrderId(paymentOutbox.getOrderId());
                paymentResult.setUserId(paymentOutbox.getUserId());
                paymentResult.setResult(paymentOutbox.getResult());

                kafkaTemplate.send(paymentResultTopic, paymentResult).get();
                paymentOutbox.setPublished(true);
                paymentOutboxRepository.save(paymentOutbox);
            } catch (Exception e) {
                log.error("Failed to publish payment result: {}", e.getMessage());
            }
        }
    }
}
