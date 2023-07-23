package com.mediscreen.history.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class HistoryDTO {

    private String id;

    private Integer patId;

    private String patient;

    private LocalDate creationDate;

    private String notes;

}