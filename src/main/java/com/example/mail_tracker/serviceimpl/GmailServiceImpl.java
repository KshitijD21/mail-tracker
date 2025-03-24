package com.example.mail_tracker.serviceimpl;

import com.example.mail_tracker.service.GmailService;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePartHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class GmailServiceImpl implements GmailService {

    @Autowired
    private Gmail gmail;

    @Override
    public String fetchSubject(String messageId)  throws IOException {
        Message message = gmail.users().messages().get("me", messageId).setFormat("metadata").execute();
        for (MessagePartHeader header : message.getPayload().getHeaders()) {
            if ("Subject".equals(header.getName())) {
                return header.getValue();
            }
        }
        return "";
    }

    @Override
    public String fetchRecipient(String messageId) throws IOException {
        Message message = gmail.users().messages().get("me", messageId).setFormat("metadata").execute();
        for (MessagePartHeader header : message.getPayload().getHeaders()) {
            if ("To".equals(header.getName())) {
                return header.getValue();
            }
        }
        return "";
    }

    @Override
    public List<Message> listMessages(String userId) throws IOException {
        List<Message> messages = new ArrayList<>();
        String pageToken = null;

        do {
            // Fetch messages from Gmail API
            ListMessagesResponse response = gmail.users().messages()
                    .list(userId)
                    .setPageToken(pageToken)
                    .execute();

            System.out.println("response " + response);

            // Add messages to the list
            if (response.getMessages() != null) {
                messages.addAll(response.getMessages());
            }

            // Update the page token
            pageToken = response.getNextPageToken();
        } while (pageToken != null);

        return messages;
    }
}
