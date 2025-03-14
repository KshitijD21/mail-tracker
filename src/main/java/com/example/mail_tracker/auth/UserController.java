package com.example.mail_tracker.auth;

import com.example.mail_tracker.entities.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    public String login(@RequestBody Users user) {
        System.out.println(user);
        return userService.verify(user);
    }

    @GetMapping("/allUser")
    public List<Users> allUser() {
        return userService.getAllUsers();
    }

}
