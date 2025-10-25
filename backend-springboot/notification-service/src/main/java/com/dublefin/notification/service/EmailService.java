package com.dublefin.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom("noreply@dublefin.com");
            
            mailSender.send(message);
        } catch (Exception e) {
            // Log error
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    public void sendTransactionAlert(String email, String accountNumber, Double amount, String type) {
        String subject = "Transaction Alert - DubleFin Banking";
        String body = String.format(
            "Dear Customer,\n\n" +
            "A %s transaction of $%.2f has been processed on your account %s.\n\n" +
            "If you did not authorize this transaction, please contact us immediately.\n\n" +
            "Best regards,\n" +
            "DubleFin Banking Team",
            type, amount, accountNumber
        );
        sendEmail(email, subject, body);
    }
}

