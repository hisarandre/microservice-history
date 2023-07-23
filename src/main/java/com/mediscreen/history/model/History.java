package com.mediscreen.history.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Document("histories")
public class History {

    @Id
    private String id;

    private Integer patId;

    private String patient;

    private LocalDate creationDate;

    @Indexed
    private String notes;

}
