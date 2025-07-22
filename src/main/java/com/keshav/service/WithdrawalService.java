package com.keshav.service;

import com.keshav.model.User;
import com.keshav.model.Withdrawal;

import java.util.List;

public interface WithdrawalService {
    Withdrawal requestWithdrawal(Long amount, User user);

    Withdrawal proceedWithWithdrawal(Long withdrawalId,boolean accept) throws Exception;

    List<Withdrawal>getUserWithdrawalHistory(User user);

    List<Withdrawal>getAllWithdrawalRequests();



}
