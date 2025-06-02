package org.kuraterut.repository;

import org.kuraterut.model.entity.OrderOutbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderOutboxRepository extends JpaRepository<OrderOutbox, Long> {
    List<OrderOutbox> findByProcessedFalseOrderByCreatedAtAsc();

    @Modifying
    @Query("UPDATE OrderOutbox o SET o.processed = true WHERE o.id = :id")
    void markAsProcessed(@Param("id") Long id);
}
