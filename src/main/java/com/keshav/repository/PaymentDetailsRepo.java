package com.keshav.repository;

import com.keshav.model.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentDetailsRepo extends JpaRepository<PaymentDetails, Long> {
    PaymentDetails findByUserId(Long userId);
}
