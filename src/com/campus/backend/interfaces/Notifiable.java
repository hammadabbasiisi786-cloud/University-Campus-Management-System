package com.campus.backend.interfaces;

import java.io.Serializable;

public interface Notifiable extends Serializable {
    void sendNotification(String message);
}

