package com.example.mail_tracker.auth;

import com.example.mail_tracker.common.ApiResponse;
import com.example.mail_tracker.entities.LoginRequest;
import com.example.mail_tracker.entities.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Users register(@RequestBody Users user) {
        System.out.println("user in register " + user);
        return userService.register(user);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("loginRequest " + loginRequest);
        return userService.verify(loginRequest);
    }

    @GetMapping("/allUser")
    public List<Users> allUser() {
        return userService.getAllUsers();
    }

}
