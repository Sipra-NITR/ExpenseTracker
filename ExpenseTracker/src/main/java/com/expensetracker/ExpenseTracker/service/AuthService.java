package com.expensetracker.ExpenseTracker.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.expensetracker.ExpenseTracker.dto.LoginRequest;
import com.expensetracker.ExpenseTracker.dto.RegisterRequest;
import com.expensetracker.ExpenseTracker.dto.LoginResponse;
import com.expensetracker.ExpenseTracker.model.User;
import com.expensetracker.ExpenseTracker.repository.UserRepository;
import com.expensetracker.ExpenseTracker.security.JwtUtil;


@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // Register User
    public String register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return "Email already exists!";
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        // Encrypt Password
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        return "User Registered Successfully";
    }

    // Login User
    public LoginResponse login(LoginRequest request) {

    Optional<User> optionalUser =
            userRepository.findByEmail(request.getEmail());

    if (optionalUser.isEmpty()) {
        throw new RuntimeException("Invalid Email");
    }

    User user = optionalUser.get();

    if (!passwordEncoder.matches(
            request.getPassword(),
            user.getPassword())) {

        throw new RuntimeException("Invalid Password");
    }

    String token = jwtUtil.generateToken(user.getEmail());

    return new LoginResponse(
            token,
            user.getId(),
            user.getName(),
            user.getEmail()
    );
}

}