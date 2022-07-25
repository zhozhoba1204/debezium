package com.blinov.kafka.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "result_document")
public class DocumentEntity {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @JsonProperty("created_date")
    @Column(name = "created_date", nullable = false)
    private LocalDate createdDate;

    @JsonProperty("updated_date")
    @Column(name = "updated_date", nullable = false)
    private LocalDate updatedDate;
}
