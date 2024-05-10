package com.pi.farmease.services;

import com.pi.farmease.dao.NotificationRepository;
import com.pi.farmease.entities.Notification;
import com.pi.farmease.entities.Product;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class NotificationService implements NotificationSer{
    private NotificationRepository notificationRepository;

    public void sendNotificationToAdmin(String message) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setTimestamp(LocalDateTime.now());
        notificationRepository.save(notification);
    }
    @Override
    public List<Notification> selectAll() {
        return notificationRepository.findAll();
    }

}
