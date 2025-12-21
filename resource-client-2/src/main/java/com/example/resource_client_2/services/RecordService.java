package com.example.resource_client_2.services;

import com.example.resource_client_2.entity.RecordEntity;
import com.example.resource_client_2.mappers.RecordMapper;
import com.example.resource_client_2.models.RecordRequestModel;
import com.example.resource_client_2.models.RecordResponseModel;
import com.example.resource_client_2.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecordService {

    private final RecordRepository recordRepository;
    private final RecordMapper recordMapper;

    // ------------------------------------------------
    // CREATE
    // ------------------------------------------------
    public RecordResponseModel createRecord(RecordRequestModel request) {
        RecordEntity entity = recordMapper.toEntity(request);
        RecordEntity saved = recordRepository.save(entity);
        return recordMapper.toResponseModel(saved);
    }


    // ------------------------------------------------
    // GET ALL
    // ------------------------------------------------
    public List<RecordResponseModel> getAllRecords() {
        return recordRepository.findAll()
                .stream()
                .map(recordMapper::toResponseModel)
                .collect(Collectors.toList());
    }


    // ------------------------------------------------
    // GET BY ID (UUID based)
    // ------------------------------------------------
    public RecordResponseModel getRecordById(String id) {
        UUID uuid = toUUID(id);

        RecordEntity entity = recordRepository.findById(uuid)
                .orElseThrow(() -> new RuntimeException("Record not found: " + id));

        return recordMapper.toResponseModel(entity);
    }


    // ------------------------------------------------
    // UPDATE (UUID based)
    // ------------------------------------------------
    public RecordResponseModel updateRecord(String id, RecordRequestModel request) {
        UUID uuid = toUUID(id);

        RecordEntity entity = recordRepository.findById(uuid)
                .orElseThrow(() -> new RuntimeException("Record not found: " + id));

        entity.setTitle(request.getTitle());
        entity.setDescription(request.getDescription());

        RecordEntity updated = recordRepository.save(entity);
        return recordMapper.toResponseModel(updated);
    }


    // ------------------------------------------------
    // DELETE (UUID based)
    // ------------------------------------------------
    public void deleteRecord(String id) {
        UUID uuid = toUUID(id);

        if (!recordRepository.existsById(uuid)) {
            throw new RuntimeException("Record not found: " + id);
        }

        recordRepository.deleteById(uuid);
    }


    // ------------------------------------------------
    // STRING → UUID Conversion
    // ------------------------------------------------
    private UUID toUUID(String id) {
        try {
            return UUID.fromString(id);
        } catch (Exception e) {
            throw new RuntimeException("Invalid UUID format: " + id);
        }
    }
}
