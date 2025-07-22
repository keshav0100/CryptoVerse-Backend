package com.keshav.controller;

import com.keshav.config.JwtProvider;
import com.keshav.model.TwoFactorOTP;
import com.keshav.model.User;
import com.keshav.repository.TwoFactorOTPRepo;
import com.keshav.repository.UserRepo;
import com.keshav.response.AuthResponse;
import com.keshav.service.CustomUserDetailsService;
import com.keshav.service.EmailService;
import com.keshav.service.TwoFactorOTPService;
import com.keshav.service.WatchListService;
import com.keshav.utils.OTP;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class Auth {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private TwoFactorOTPService twoFactorOTPService;

    @Autowired
    private WatchListService watchListService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> register(@RequestBody User user) throws Exception {

        User isEmailExist = userRepo.findByEmail(user.getEmail());
        if(isEmailExist != null){
            throw new Exception("Email already exists with another account");
        }

        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        newUser.setEmail(user.getEmail());
        newUser.setFullName(user.getFullName());

        User savedUser = userRepo.save(newUser);

        watchListService.createWatchList(savedUser);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                user.getPassword()
        );

        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt = JwtProvider.generateToken(auth);

        AuthResponse res = new AuthResponse();
        res.setJwt(jwt);
        res.setStatus(true);
        res.setMessage("Register success");

        return new ResponseEntity<>(res , HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> login(@RequestBody User user) throws Exception {

        String username = user.getEmail();
        String password = user.getPassword();

        Authentication auth = authenticate(username,password);

        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt = JwtProvider.generateToken(auth);

        User authUser=userRepo.findByEmail(username);

        if(user.getTwoFactorAuth().isEnabled())
        {
                AuthResponse res = new AuthResponse();
                res.setMessage("Two Factor Auth is Enabled");
                res.setTwoFactorAuthEnabled(true);
                String otp= OTP.generateOTP();

            TwoFactorOTP oldTwoFactorOTP=twoFactorOTPService.findByUser(authUser.getId());
            if(oldTwoFactorOTP!=null)
            {
                twoFactorOTPService.deleteTwoFactorOTP(oldTwoFactorOTP);
            }
            TwoFactorOTP newTwoFactorOTP=twoFactorOTPService.createTwoFactorOTP(authUser,otp,jwt);

            emailService.sendVerificationOTPEmail(username,otp);

            res.setSession(newTwoFactorOTP.getId());
            return new ResponseEntity<>(res , HttpStatus.ACCEPTED);

        }

        AuthResponse res = new AuthResponse();
        res.setJwt(jwt);
        res.setStatus(true);
        res.setMessage("Login success");

        return new ResponseEntity<>(res , HttpStatus.CREATED);
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        if(userDetails == null)
        {
            throw new BadCredentialsException("Invalid username");
        }
        if(!password.equals(userDetails.getPassword()))
        {
            throw new BadCredentialsException("Invalid password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    @PostMapping("/two-factor/otp/{otp}")
    public ResponseEntity<AuthResponse> verifySignInOtp(@PathVariable String otp,@RequestParam String id) throws Exception {
        TwoFactorOTP twoFactorOTP=twoFactorOTPService.findById(id);
        if(twoFactorOTPService.verifyTwoFactorOTP(twoFactorOTP,otp))
        {
            AuthResponse res = new AuthResponse();
            res.setMessage("Two factor authentication verified");
            res.setTwoFactorAuthEnabled(true);
            res.setJwt(twoFactorOTP.getJwt());
            return new ResponseEntity<>(res , HttpStatus.OK);
        }
        throw new Exception("Invalid otp");
    }
}
