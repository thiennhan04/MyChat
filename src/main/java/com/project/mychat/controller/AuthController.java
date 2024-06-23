package com.project.mychat.controller;

import com.project.mychat.config.TokenProvider;
import com.project.mychat.entity.User;
import com.project.mychat.exception.UserException;
import com.project.mychat.repository.UserRepository;
import com.project.mychat.request.LoginRequest;
import com.project.mychat.response.AuthResponse;
import com.project.mychat.service.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    private TokenProvider tokenProvider;
    private CustomUserService customUserService;
    public AuthController(
            UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder, CustomUserService customUserService){
        this.userRepository =  userRepository;
        this.passwordEncoder = passwordEncoder;
        this.customUserService = customUserService;
    }
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandle(@RequestBody User user) throws UserException {
        String email = user.getEmail();
        String full_name = user.getFull_name();
        String password = user.getPassword();
        User isUser = userRepository.findByEmail(email);
        if(isUser != null){
            throw new UserException("Email is used with another account " + email);
        }
        User createUser = new User();
        createUser.setEmail(email);
        createUser.setFull_name(full_name);
        createUser.setPassword(passwordEncoder.encode(password));
        userRepository.save(createUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        AuthResponse response = new AuthResponse(jwt, true);
        return new ResponseEntity<AuthResponse>(response, HttpStatus.ACCEPTED);
    }
    public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest loginRequest){
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        Authentication  authentication = authentication(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        AuthResponse res = new AuthResponse(jwt, true);
        return new ResponseEntity<AuthResponse>(res, HttpStatus.ACCEPTED);
    }
    public Authentication authentication(String Username, String password){
        UserDetails userDetails = customUserService.loadUserByUsername(Username);
        if(userDetails == null){
            throw new BadCredentialsException("invalid username");
        }
        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("invalid password or username");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
