package com.pi.farmease.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class EmailNotificationService {
    JavaMailSender javaMailSender;

    public void sendStockNotification(String to, String productName) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo(to);
        helper.setSubject("Notification de rupture de stock");
        helper.setText("Le produit " + productName + " est en rupture de stock.");
        javaMailSender.send(mimeMessage);
    }

}
