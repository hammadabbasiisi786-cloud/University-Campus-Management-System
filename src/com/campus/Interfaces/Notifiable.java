package com.campus.Interfaces;

import java.io.Serializable;

public interface Notifiable extends Serializable {
    void sendNotification(String message);
}
