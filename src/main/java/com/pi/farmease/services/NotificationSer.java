package com.pi.farmease.services;

import com.pi.farmease.entities.Notification;

import java.util.List;

public interface NotificationSer {
    public void sendNotificationToAdmin(String message) ;
    public List<Notification> selectAll();
}
