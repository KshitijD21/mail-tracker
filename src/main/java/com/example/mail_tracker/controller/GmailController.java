package com.example.mail_tracker.controller;

import com.example.mail_tracker.service.GmailService;
import com.google.api.services.gmail.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/gmail")
public class GmailController {

    @Autowired
    private GmailService gmailService;

    @GetMapping("/metadata")
    public String getEmailMetadata(@RequestParam String messageId) throws IOException {
        String subject = gmailService.fetchSubject(messageId);
        String recipient = gmailService.fetchRecipient(messageId);
        return "Subject: " + subject + ", Recipient: " + recipient;
    }

    @GetMapping("/messages")
    public List<Message> listMessages(@RequestParam(defaultValue = "me") String userId) throws IOException {
        return gmailService.listMessages(userId);
    }
}
