package com.keshav.service;

import com.keshav.Domain.OrderType;
import com.keshav.model.Order;
import com.keshav.model.User;
import com.keshav.model.Wallet;
import com.keshav.repository.WalletRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WalletRepo walletRepo;

    @Override
    public Wallet getUserWallet(User user) {
        Wallet wallet = walletRepo.findByUserId(user.getId());
        if (wallet == null) {
            wallet = new Wallet();
            wallet.setUser(user);
            wallet.setBalance(BigDecimal.ZERO); // Initialize with zero balance
            walletRepo.save(wallet);
        }
        return wallet;
    }

    @Override
    public Wallet addBalance(Wallet wallet, Long money) {
        BigDecimal balance = wallet.getBalance();
        if (balance == null) {
            balance = BigDecimal.ZERO;
        }
        BigDecimal newBalance = balance.add(BigDecimal.valueOf(money));
        wallet.setBalance(newBalance);
        return walletRepo.save(wallet);
    }

    @Override
    public Wallet findById(Long id) throws Exception {
        Optional<Wallet> wallet = walletRepo.findById(id);
        if (wallet.isPresent()) {
            return wallet.get();
        }
        throw new Exception("Wallet not found");
    }

    @Override
    @Transactional
    public Wallet walletToWalletTransfer(User sender, Wallet receiverWallet, Long amount) throws Exception {
        Wallet senderWallet = getUserWallet(sender);

        if (senderWallet.getBalance().compareTo(BigDecimal.valueOf(amount)) < 0) {
            throw new Exception("Insufficient funds");
        }

        // Re-fetch the receiver's wallet within the transaction to ensure it's a managed entity
        Wallet managedReceiverWallet = walletRepo.findById(receiverWallet.getId())
                .orElseThrow(() -> new Exception("Receiver wallet not found"));

        if (sender.getId().equals(managedReceiverWallet.getUser().getId())) {
            throw new Exception("Cannot transfer to your own wallet");
        }

        BigDecimal senderBalance = senderWallet.getBalance().subtract(BigDecimal.valueOf(amount));
        senderWallet.setBalance(senderBalance);
        walletRepo.save(senderWallet);

        if(managedReceiverWallet.getBalance()==null){
            managedReceiverWallet.setBalance(BigDecimal.valueOf(0));
        }

        BigDecimal receiverBalance = managedReceiverWallet.getBalance().add(BigDecimal.valueOf(amount));
        managedReceiverWallet.setBalance(receiverBalance);
        walletRepo.save(managedReceiverWallet);

        return senderWallet;
    }

    @Override
    public Wallet payOrderPayment(Order order, User user) throws Exception {
        Wallet wallet = getUserWallet(user);
        if(order.getOrderItem().equals(OrderType.BUY))
        {
            BigDecimal newBalance=wallet.getBalance().subtract(order.getPrice());
            if(newBalance.compareTo(BigDecimal.ZERO)<0)
            {
                throw new Exception("Insufficient funds for this transaction");
            }
            wallet.setBalance(newBalance);
        }
        else {
            BigDecimal newBalance=wallet.getBalance().add(order.getPrice());
            wallet.setBalance(newBalance);
        }
        walletRepo.save(wallet);
        return wallet;
    }
}
