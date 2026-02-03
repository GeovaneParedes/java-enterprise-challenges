package com.enterprise.notification;

import java.util.Map;

public record NotificationStats(
    long totalNotifications,
    Map<String, Long> countByCurrency
) {}
