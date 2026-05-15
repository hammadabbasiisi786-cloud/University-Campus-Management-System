package com.campus.Interfaces;

public interface Notifiable {

    // Contract: implementing classes must define how they handle incoming notification messages
    void sendNotification(String message);
}