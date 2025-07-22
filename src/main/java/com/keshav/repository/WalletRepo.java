package com.keshav.repository;

import com.keshav.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepo extends JpaRepository<Wallet, Long> {
    Wallet findByUserId(Long userId);
}
