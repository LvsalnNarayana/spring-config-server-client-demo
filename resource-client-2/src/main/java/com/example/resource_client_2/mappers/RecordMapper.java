package com.example.resource_client_2.mappers;

import com.example.resource_client_2.entity.RecordEntity;
import com.example.resource_client_2.models.RecordRequestModel;
import com.example.resource_client_2.models.RecordResponseModel;
import org.springframework.stereotype.Component;

@Component
public class RecordMapper {

    // --------------------------------------------------------
    // Convert RequestModel → Entity
    // --------------------------------------------------------
    public RecordEntity toEntity(RecordRequestModel request) {
        if (request == null) {
            return null;
        }

        return RecordEntity.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .build();
    }


    // --------------------------------------------------------
    // Convert Entity → ResponseModel
    // --------------------------------------------------------
    public RecordResponseModel toResponseModel(RecordEntity entity) {
        if (entity == null) {
            return null;
        }

        return RecordResponseModel.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
