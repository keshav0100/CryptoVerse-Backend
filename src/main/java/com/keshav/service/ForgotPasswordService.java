package com.keshav.service;

import com.keshav.Domain.VerificationType;
import com.keshav.model.ForgotPasswordToken;
import com.keshav.model.User;

public interface ForgotPasswordService {
    ForgotPasswordToken createToken(User user,
                                      String id,String otp,
                                      VerificationType verificationType,
                                      String sendTo);
    ForgotPasswordToken findById(String id);

    ForgotPasswordToken findByUser(Long userId);

    void deleteToken(ForgotPasswordToken token);
}
