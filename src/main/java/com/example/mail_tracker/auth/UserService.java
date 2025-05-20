package com.example.mail_tracker.auth;


import com.example.mail_tracker.common.ApiResponse;
import com.example.mail_tracker.entities.LoginRequest;
import com.example.mail_tracker.entities.Users;
import com.example.mail_tracker.repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserRepo repo;

    //comments

    private AuthenticationManager authenticationManager;

    @Autowired
    public UserService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    private JWTService jwtService;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10);

    public Users register (Users user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Users savedUser =  repo.save(user);
        System.out.println("user register id: " + savedUser.getId());

        return savedUser;
    }

    public ResponseEntity<ApiResponse<String>> verify(LoginRequest users) {
        System.out.println("Inside verify");

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(users.getEmail(), users.getPassword())
            );

            System.out.println("authentication.isAuthenticated(): " + authentication.isAuthenticated());

            if (authentication.isAuthenticated()) {
                Users authenticatedUser = repo.findByEmail(users.getEmail());
                if (authenticatedUser != null) {
                    System.out.println("Authenticated user: " + authenticatedUser);
                    String token = jwtService.generateToken(users.getEmail(), authenticatedUser.getId());

                    ApiResponse<String> response = new ApiResponse<>("success", "Login successful", token, null);
                    System.out.println("response value is "+ response);
                    return ResponseEntity.ok(response);
                } else {
                    ApiResponse<String> response = new ApiResponse<>("error", "User not found", null, null);
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
                }
            } else {
                ApiResponse<String> response = new ApiResponse<>("error", "Authentication failed", null, null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

        } catch (Exception e) {
            ApiResponse<String> response = new ApiResponse<>("error", "Invalid email or password", null, null);
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    public List<Users> getAllUsers() {
        System.out.println(" inside get all users " );
        List<Users> allData  = repo.findAll();
        System.out.println(" allData " + allData);
        return allData;

    }
}
