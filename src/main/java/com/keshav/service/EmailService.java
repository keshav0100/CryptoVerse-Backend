package com.keshav.service;

import jakarta.mail.MessagingException;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    JavaMailSender mailSender;
    public void sendVerificationOTPEmail(String email,String otp) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,"utf-8");

        String subject="Verify OTP";
        String text="Hello from CryptoVerse! Your verification code is "+otp;

        helper.setSubject(subject);
        helper.setText(text);
        helper.setTo(email);

        try{
            mailSender.send(mimeMessage);
        }
        catch (MailException e)
        {
            throw new MailSendException(e.getMessage());
        }

    }
}
