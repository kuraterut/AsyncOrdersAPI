package org.kuraterut.repository;

import org.kuraterut.model.entity.PaymentOutbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentOutboxRepository extends JpaRepository<PaymentOutbox, Long> {
    List<PaymentOutbox> findTop100ByPublishedFalse();
}
