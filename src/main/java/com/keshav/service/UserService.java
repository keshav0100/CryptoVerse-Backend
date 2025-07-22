package com.keshav.service;

import com.keshav.Domain.VerificationType;
import com.keshav.model.User;

public interface UserService {

    public User findUserByJwt(String jwt) throws Exception;
    public User findUserByEmail(String email) throws Exception;
    public User findUserById(Long userId) throws Exception;

    public User enableTwoFactorAuth(VerificationType verificationtype,String sendTo,User user);

    User updatePassword(User user,String newPassword);

}
