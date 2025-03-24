package com.example.mail_tracker.service;

import com.google.api.services.gmail.model.Message;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

public interface GmailService {

    String fetchSubject(String messageId) throws IOException;;

    String fetchRecipient(String messageId)  throws IOException;

    List<Message> listMessages(String userId) throws IOException;
}
