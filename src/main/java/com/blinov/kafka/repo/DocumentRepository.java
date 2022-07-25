package com.blinov.kafka.repo;

import com.blinov.kafka.model.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DocumentRepository extends JpaRepository<DocumentEntity, UUID> {
}
