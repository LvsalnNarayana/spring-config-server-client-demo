package com.example.resource_client_1.repository;

import com.example.resource_client_1.entity.RecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RecordRepository extends JpaRepository<RecordEntity, UUID> {

    // Add custom query methods here if needed
}
