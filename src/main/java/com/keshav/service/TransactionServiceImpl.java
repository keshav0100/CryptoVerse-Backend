//package com.keshav.service;
//
//import com.keshav.Domain.WalletTransactionType;
//import com.keshav.model.Wallet;
//import com.keshav.model.WalletTransaction;
//import com.keshav.repository.WalletRepo;
//import com.keshav.repository.WalletTransactionRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//
//@Service
//public class TransactionServiceImpl implements TransactionService {
//
//    @Autowired
//    private WalletRepo walletRepo;
//
//    @Override
//    public WalletTransaction createTransaction(Wallet wallet, WalletTransactionType type, Long transferId, String purpose, Long amount) {
//        WalletTransaction transaction = new WalletTransaction();
//
//        transaction.setWallet(wallet);
//        transaction.setTransactionType(type);
//        transaction.setTransferId(transferId);
//        transaction.setPurpose(purpose);
//        transaction.setAmount(BigDecimal.valueOf(amount)); // or convert to rupees if needed
//
//        transaction.setTimestamp(LocalDateTime.now());
//
//        return walletRepo.save(transaction);
//    }
//}
