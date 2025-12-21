package com.example.resource_client_1.models;

import com.example.resource_client_1.models.BaseModel;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordModel extends BaseModel {

    private UUID id;

    private String title;

    private String description;

    private Instant createdAt;

    private Instant updatedAt;
}
