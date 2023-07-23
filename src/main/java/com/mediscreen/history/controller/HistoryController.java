package com.mediscreen.history.controller;

import com.mediscreen.history.dto.HistoryDTO;
import com.mediscreen.history.service.HistoryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@RestController
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    private static Logger logger = LoggerFactory.getLogger(HistoryController.class);

    @Operation(summary = "Get a patient history by its id")
    @GetMapping(value = "/patHistory/{id}")
    public HistoryDTO getHistoryById(@PathVariable String id) {
        logger.info("History " + id + " requested");
        return historyService.getHistoryById(id);
    }

    @Operation(summary = "Get a patient history by patient id")
    @GetMapping(value = "/patHistory")
    public List<HistoryDTO> getHistoryByPatientId(@RequestParam("patId") Integer id) {
        logger.info("History for the Patient id : " + id + " requested");
        return historyService.getHistoryByPatientId(id);
    }

    @Operation(summary = "Get all patient histories ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all histories", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = HistoryDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Histories not found", content = @Content)
    })
    @GetMapping(value = "/patHistory/all")
    public List<HistoryDTO> getHistoryList() {
        logger.info("List of patient histories requested");
        return historyService.getAllHistories();
    }

    @Operation(summary = "Save a patient history")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient History added successfully", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = HistoryDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid Patient History data", content = @Content),
            @ApiResponse(responseCode = "500", description = "Failed to add Patient History", content = @Content)
    })
    @PostMapping(value = "/patHistory/add", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> addHistory(@ModelAttribute HistoryDTO historyDTO) {
        logger.info("Adding new patient history: " + historyDTO.getId());

        //Check if the patient history is saved
        if (historyService.addHistory(historyDTO)) {
            logger.info("created patient history at: " + historyDTO.getCreationDate());
            return ResponseEntity.status(HttpStatus.CREATED).body("Patient history added successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add patient history");
        }
    }

    @Operation(summary = "Update a patient history")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient history updated successfully", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = HistoryDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid patient history data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Patient history not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Failed to update patient history", content = @Content)
    })
    @PutMapping(value = "/patHistory/update/{id}")
    public ResponseEntity<String> updateHistory(@PathVariable String id, @RequestBody HistoryDTO historyDTO) {
        logger.info("Updating patient history with ID: " + id);

        // Update the patient history
        if (historyService.updateHistory(id, historyDTO)) {
            logger.info("Updated patient history with ID: " + id);
            return ResponseEntity.status(HttpStatus.OK).body("Patient history updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update patient history");
        }
    }

    @Operation(summary = "Delete a patient history by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Patient history deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Patient history not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Failed to delete patient history", content = @Content)
    })
    @DeleteMapping(value = "/patHistory/{id}")
    public ResponseEntity<String> deleteHistory(@PathVariable String id) {
        logger.info("Deleting patient history with ID: " + id);

        if (historyService.deleteHistory(id)) {
            logger.info("Deleted patient history with ID: " + id);
            return ResponseEntity.status(HttpStatus.OK).body("Patient history deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete patient history");
        }
    }
}
