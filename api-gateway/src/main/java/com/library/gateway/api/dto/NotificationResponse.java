package com.library.gateway.api.dto;

import lombok.Data;

@Data
public class NotificationResponse {
    private Long borrowingId;
    private Boolean exists;
    private String status;
}
