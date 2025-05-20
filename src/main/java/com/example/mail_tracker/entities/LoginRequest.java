package com.example.mail_tracker.entities;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
