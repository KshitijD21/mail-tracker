package com.example.mail_tracker.auth;


import com.example.mail_tracker.entities.UserPrinciple;
import com.example.mail_tracker.entities.Users;


import com.example.mail_tracker.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepo.findByUserName(username);

        if (user == null) {
            System.out.println("User not found for username: " + username);
            throw new UsernameNotFoundException("User not found");
        }

        System.out.println("User found: " + user);
        return new UserPrinciple(user);
    }


}
