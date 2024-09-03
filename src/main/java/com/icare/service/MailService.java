package com.icare.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Async
    @Transactional
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = createMail(to, subject, text);
        try {
            javaMailSender.send(message);
        } catch (MailException e) {
            throw new RuntimeException("Error sending email", e);
        }
    }

    private SimpleMailMessage createMail(String to, String subject, String textMessage) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom(fromEmail);
        message.setSubject(subject);
        message.setText(textMessage);
        return message;
    }
}