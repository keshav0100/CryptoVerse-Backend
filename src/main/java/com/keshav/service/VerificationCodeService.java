package com.keshav.service;

import com.keshav.Domain.VerificationType;
import com.keshav.model.User;
import com.keshav.model.VerificationCode;

public interface VerificationCodeService {

    VerificationCode sendVerificationOtp(User user, VerificationType verificationType);

    VerificationCode getVerificationCodeById(Long id) throws Exception;

    VerificationCode getVerificationCodeByUser(Long userId);

    void deleteVerificationCode(VerificationCode verificationCode);
}
