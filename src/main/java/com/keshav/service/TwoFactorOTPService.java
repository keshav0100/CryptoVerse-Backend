package com.keshav.service;

import com.keshav.model.TwoFactorOTP;
import com.keshav.model.User;

public interface TwoFactorOTPService {

    TwoFactorOTP createTwoFactorOTP(User user, String otp, String jwt);

    TwoFactorOTP findByUser(Long userId);

    TwoFactorOTP findById(String id);

    boolean verifyTwoFactorOTP(TwoFactorOTP twoFactorOTP,String otp);

    void deleteTwoFactorOTP(TwoFactorOTP twoFactorOTP);
}
