package com.keshav.controller;

import com.keshav.Domain.PaymentMethod;
import com.keshav.model.PaymentOrder;
import com.keshav.model.User;
import com.keshav.response.PaymentResponse;
import com.keshav.service.PaymentService;
import com.keshav.service.UserService;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController

public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserService userService;

    @PostMapping("/api/payment/{paymentMethod}/amount/{amount}")
    public ResponseEntity<PaymentResponse> paymentHandler(
            @PathVariable PaymentMethod paymentMethod,
            @PathVariable Long amount,
            @RequestHeader("Authorization")String jwt)throws
            Exception,
            RazorpayException,
            StripeException
            {
        User user = userService.findUserByJwt(jwt);
        PaymentResponse paymentResponse;
        PaymentOrder order=paymentService.createOrder(user,amount,paymentMethod);
        if(paymentMethod.equals(PaymentMethod.RAZORPAY)){
            paymentResponse=paymentService.createRazorpayPaymentLink(user,amount, order.getId());
        }
        else{
            paymentResponse=paymentService.createStripePaymentLink(user
            ,amount,order.getId());
        }
        return new ResponseEntity<>(paymentResponse, HttpStatus.CREATED);
    }


}
