package com.keshav.service;

import com.keshav.model.PaymentDetails;
import com.keshav.model.User;

public interface PaymentDetailsService {
    public PaymentDetails addPaymentDetails(String accountNo,
                                            String accountName,
                                            String ifsc,
                                            String bankName,
                                            User user
                                            );
    public PaymentDetails getUserPaymentDetails(User user);
}
