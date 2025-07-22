package com.keshav.controller;


import com.keshav.Domain.VerificationType;
import com.keshav.request.ForgotPasswordTokenRequest;
import com.keshav.model.ForgotPasswordToken;
import com.keshav.model.VerificationCode;
import com.keshav.request.ResetPasswordRequest;
import com.keshav.response.ApiResponse;
import com.keshav.response.AuthResponse;
import com.keshav.service.EmailService;
import com.keshav.model.User;
import com.keshav.service.ForgotPasswordService;
import com.keshav.service.UserService;
import com.keshav.service.VerificationCodeService;
import com.keshav.utils.OTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private EmailService emailService;
    private String jwt;

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @GetMapping("/api/users/profile")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt) throws Exception {
        User user=userService.findUserByJwt(jwt);

        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @PostMapping("/api/users/verification/{verificationType}/send-otp")
    public ResponseEntity<String> sendVerificationOtp(
            @RequestHeader("Authorization") String jwt,
            @PathVariable VerificationType verificationType) throws Exception {
        User user=userService.findUserByJwt(jwt);

        VerificationCode verificationCode=verificationCodeService.getVerificationCodeByUser(user.getId());

        if(verificationCode==null) {
    verificationCode=verificationCodeService.sendVerificationOtp(user, verificationType);
        }
        if(verificationType.equals(VerificationType.EMAIL)) {
            emailService.sendVerificationOTPEmail(user.getEmail(),verificationCode.getOtp());
        }

        return new ResponseEntity<>("Verification otp send successfully!", HttpStatus.OK);
    }

    @PatchMapping("/api/users/enable-two-factor/verify-otp/{otp}")
    public ResponseEntity<User> enableTwoFactorAuth(
            @PathVariable String otp,
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user=userService.findUserByJwt(jwt);

        VerificationCode verificationCode=verificationCodeService.getVerificationCodeByUser(user.getId());

        String sendTo=verificationCode.getVerificationType().equals(VerificationType.EMAIL)?
                verificationCode.getEmail():verificationCode.getMobile();

        boolean isVerified=verificationCode.getOtp().equals(otp);
        if(isVerified) {
            User updatedUser=userService.enableTwoFactorAuth(verificationCode.getVerificationType(),sendTo,user);

            verificationCodeService.deleteVerificationCode(verificationCode);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
        throw new Exception("Wrong otp");
    }

    @PostMapping("/auth/users/reset-password/send-otp")
    public ResponseEntity<AuthResponse> sendForgetPasswordOtp(
            @RequestBody ForgotPasswordTokenRequest req) throws Exception {

        User user=userService.findUserByEmail(req.getSendTo());
        String otp= OTP.generateOTP();
        UUID uuid=UUID.randomUUID();
        String id=uuid.toString();

        ForgotPasswordToken token=forgotPasswordService.findByUser(user.getId());
        if(token==null) {
            token=forgotPasswordService.createToken(user,id,otp,req.getVerificationType(),req.getSendTo());
        }

        if(req.getVerificationType().equals(VerificationType.EMAIL)) {
            emailService.sendVerificationOTPEmail(
                    user.getEmail(),
                    token.getOtp());
        }

        AuthResponse response=new AuthResponse();
        response.setSession(token.getId());
        response.setMessage("Password Reset OTP sent successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/auth/users/reset-password/verify-otp/")
    public ResponseEntity<ApiResponse> resetPasswordOtp(
            @RequestParam String id,
            @RequestBody ResetPasswordRequest req,
            @RequestHeader("Authorization") String jwt) throws Exception {

        ForgotPasswordToken forgotPasswordToken=forgotPasswordService.findById(id);

        boolean isVerified=forgotPasswordToken.getVerificationType().equals(req.getOtp());
        if(isVerified) {
            userService.updatePassword(forgotPasswordToken.getUser(),req.getPassword());
            ApiResponse res=new ApiResponse();
            res.setMessage("Password updated successfully");
            return new ResponseEntity<>(res,HttpStatus.ACCEPTED);
        }
    throw new Exception("Wrong otp");
    }


}

