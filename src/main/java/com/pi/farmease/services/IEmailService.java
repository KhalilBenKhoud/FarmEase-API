package com.pi.farmease.services;

import com.pi.farmease.entities.Insurance;

public interface IEmailService {
    void sendSimpleMailMessage(Insurance i, String to);
    void sendHtmlEmail(String name, String to, String token);
}