package com.example.resource_client_2.models;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponseModel extends BaseModel {
    private String message;
    private Instant timestamp;
    private Object details;
}
