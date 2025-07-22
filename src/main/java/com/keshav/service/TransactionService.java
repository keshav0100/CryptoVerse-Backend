package com.keshav.service;

import com.keshav.model.Wallet;
import com.keshav.model.WalletTransaction;
import com.keshav.Domain.WalletTransactionType;

import java.math.BigDecimal;

public interface TransactionService {
    WalletTransaction createTransaction(Wallet wallet, WalletTransactionType type, Long transferId, String purpose, Long amount);
}
