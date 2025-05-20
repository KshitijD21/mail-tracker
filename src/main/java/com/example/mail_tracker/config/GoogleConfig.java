package com.example.mail_tracker.config;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.client.util.store.MemoryDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

@Configuration
public class GoogleConfig {

//    private static final String APPLICATION_NAME = "Gmail API Spring Boot";
//    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
//    private static final String TOKENS_DIRECTORY_PATH = "tokens";
//    private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_READONLY);
//    private static final String CREDENTIALS_FILE_PATH = "credentials.json";
//
//    @Bean
//    public Gmail gmailService() throws Exception {
//        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//        return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
//                .setApplicationName(APPLICATION_NAME)
//                .build();
//    }
//
//    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws Exception {
//
////        File clientSecretsFile = new File(CREDENTIALS_FILE_PATH);
//
//        ClassPathResource resource = new ClassPathResource(CREDENTIALS_FILE_PATH);
//
//        System.out.println("clientSecretsFile " + resource);
//
//        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(resource.getInputStream()));
//
//        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
//                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
//                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
////                .setDataStoreFactory(new MemoryDataStoreFactory())
//                .setAccessType("offline")
//                .build();
//
//        System.out.println("flow " + flow.toString());
//
////        LocalServerReceiver receiver = new LocalServerReceiver.Builder()
////                .setPort(8080)
////                .build();
//
//        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
//    }




private static final String APPLICATION_NAME = "Gmail API Spring Boot";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "credentials.json";

    @Bean
    public Gmail gmailService() throws Exception {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, null)  // No credentials at startup
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public GoogleAuthorizationCodeFlow getFlow() throws Exception {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        ClassPathResource resource = new ClassPathResource(CREDENTIALS_FILE_PATH);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(resource.getInputStream()));

        return new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
    }
}
