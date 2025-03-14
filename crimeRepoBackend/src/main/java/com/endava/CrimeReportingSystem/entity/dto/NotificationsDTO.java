package com.endava.CrimeReportingSystem.entity.dto;

import java.time.LocalDateTime;

public record NotificationsDTO(
    int notificationId,
    String notificationTitle,
    String notificationMessage,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Boolean isActive,
    String toUserType,
    UsersDTO user
) {}
