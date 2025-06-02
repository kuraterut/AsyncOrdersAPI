package org.kuraterut.repository;

import org.kuraterut.model.entity.PaymentInbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentInboxRepository extends JpaRepository<PaymentInbox, Long> {
    List<PaymentInbox> findTop100ByProcessedFalseOrderByCreatedAtAsc();

    @Modifying
    @Query("UPDATE PaymentInbox pi SET pi.processed = true WHERE pi.id = :id")
    void markAsProcessed(@Param("id") Long id);
}
