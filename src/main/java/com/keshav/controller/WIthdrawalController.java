package com.keshav.controller;

import com.keshav.model.User;
import com.keshav.model.Wallet;
import com.keshav.model.WalletTransaction;
import com.keshav.model.Withdrawal;
import com.keshav.service.UserService;
import com.keshav.service.WalletService;
import com.keshav.service.WithdrawalService;
import lombok.With;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("/api/withdrawal")
public class WIthdrawalController {

    @Autowired
    private WithdrawalService withdrawalService;

    @Autowired
    private UserService userService;
    @Autowired
    private WalletService walletService;

//    @Autowired
//    private WithdrawalTransactionService withdrawalTransactionService;

    @PostMapping("/api/withdrawal/{amount}")
    public ResponseEntity<?>withdrawalRequest(
            @PathVariable Long amount,
            @RequestHeader("Authorization")String jwt)throws Exception{
        User user=userService.findUserByJwt(jwt);
        Wallet userWallet=walletService.getUserWallet(user);

        Withdrawal withdrawal=withdrawalService.requestWithdrawal(amount,user);
        walletService.addBalance(userWallet,-withdrawal.getAmount());
    return new ResponseEntity<>(withdrawal, HttpStatus.OK);
    }

    @PatchMapping("/api/admin/withdrawal/{id}/proceed/{accept}")
    public ResponseEntity<?> proceedWithdrawal(
            @PathVariable Long id,
            @PathVariable boolean accept,
            @RequestHeader("Authorization")String jwt)throws Exception{
        User user=userService.findUserByJwt(jwt);
        Withdrawal withdrawal=withdrawalService.proceedWithWithdrawal(id,accept);

        Wallet userWallet=walletService.getUserWallet(user);
        if(!accept)
        {
            walletService.addBalance(userWallet, withdrawal.getAmount());
        }
        return new ResponseEntity<>(withdrawal, HttpStatus.OK);
    }


    @GetMapping("/api/withdrawal")
    public ResponseEntity<List<Withdrawal>>getWithdrawalHistory(
            @RequestHeader("Authorization")String jwt)throws Exception{
        User user=userService.findUserByJwt(jwt);
        List<Withdrawal> withdrawal=withdrawalService.getUserWithdrawalHistory(user);
        return new ResponseEntity<>(withdrawal, HttpStatus.OK);
    }

    @GetMapping("/api/admin/withdrawal")
    public ResponseEntity<List<Withdrawal>>getAllWithdrawalRequest(
            @RequestHeader("Authorization")String jwt)throws Exception{
        User user=userService.findUserByJwt(jwt);
        List<Withdrawal> withdrawal=withdrawalService.getAllWithdrawalRequests();
        return new ResponseEntity<>(withdrawal, HttpStatus.OK);
    }







}
