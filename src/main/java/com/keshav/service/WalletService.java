package com.keshav.service;

import com.keshav.model.Order;
import com.keshav.model.User;
import com.keshav.model.Wallet;

public interface WalletService {

    Wallet getUserWallet (User user);

    Wallet addBalance (Wallet wallet,Long money);

    Wallet findById (Long id) throws Exception;

    Wallet walletToWalletTransfer (User sender,Wallet receiverWallet,Long amount) throws Exception;

    Wallet payOrderPayment(Order order, User user) throws Exception;
}
