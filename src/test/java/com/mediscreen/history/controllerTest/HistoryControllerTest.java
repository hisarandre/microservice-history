package com.mediscreen.history.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediscreen.history.controller.HistoryController;
import com.mediscreen.history.dto.HistoryDTO;
import com.mediscreen.history.repository.HistoryRepository;
import com.mediscreen.history.service.HistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureDataMongo
@SpringBootTest
public class HistoryControllerTest {

    @Mock
    private HistoryService historyService;
    @InjectMocks
    private HistoryController historyController;
    @Autowired
    public MockMvc mvc;
    @Autowired
    private HistoryRepository repository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.standaloneSetup(historyController).build();
    }


    @Test
    void testGetPatientById() throws Exception {
        // GIVEN
        // There are already a history in db
        HistoryDTO historyDTO = new HistoryDTO();
        historyDTO.setId("1");

        // WHEN
        // I request the history of the patient
        when(historyService.getHistoryById("1")).thenReturn(historyDTO);


        // THEN
        // I get the information about the patient I requested
        HistoryDTO result = historyController.getHistoryById("1");
        mvc.perform(MockMvcRequestBuilders.get("/patHistory/1")).andExpect(status().isOk());
        assertNotNull(result);
    }

    @Test
    void testGetPatientByIdWithTheWrongId() throws Exception {
        // GIVEN
        // The history with ID "1" exists in db
        HistoryDTO historyDTO = new HistoryDTO();
        historyDTO.setId("1");

        // WHEN
        // I request the history with ID "2" (which does not exist)
        when(historyService.getHistoryById("2")).thenReturn(null);

        // THEN
        // I don't get the information about the patient I requested
        HistoryDTO result = historyController.getHistoryById("2");
        assertNull(result);
    }

    @Test
    void testGetHistoryByPatientId() throws Exception {
        // GIVEN
        // There are already a history in db
        List<HistoryDTO> histories = new ArrayList<>();
        HistoryDTO historyDTO = new HistoryDTO();
        historyDTO.setId("1");
        historyDTO.setPatId(1);
        histories.add(historyDTO);

        // WHEN
        // I request the history of the patient
        when(historyService.getHistoryByPatientId(1)).thenReturn(histories);


        // THEN
        // I get the information about the patient I requested
        List<HistoryDTO> result = historyController.getHistoryByPatientId(1);
        mvc.perform(MockMvcRequestBuilders.get("/patHistory?patId=1")).andExpect(status().isOk());
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testGetAllHistories() throws Exception {
        // GIVEN
        // There are already a history in db
        List<HistoryDTO> histories = new ArrayList<>();
        HistoryDTO historyDTO = new HistoryDTO();
        historyDTO.setId("1");
        historyDTO.setPatId(1);
        histories.add(historyDTO);

        // WHEN
        // I request all histories
        when(historyService.getAllHistories()).thenReturn(histories);


        // THEN
        // I get the information about all histories
        List<HistoryDTO> result = historyController.getHistoryList();
        mvc.perform(MockMvcRequestBuilders.get("/patHistory/all")).andExpect(status().isOk());
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testAddHistory() throws Exception {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("id", "1");
        formData.add("patId", "1");
        formData.add("patient", "Doe");
        formData.add("creationDate", "2000-10-10");
        formData.add("notes", "123 Main Street");

        // WHEN
        when(historyService.addHistory(any(HistoryDTO.class))).thenReturn(true);

        // THEN
        mvc.perform(MockMvcRequestBuilders.post("/patHistory/add")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .params(formData))
                .andExpect(status().isCreated());
    }

    @Test
    void testUpdateHistory() throws Exception {
        // GIVEN
        HistoryDTO historyDTO = new HistoryDTO();
        historyDTO.setId("1");
        historyDTO.setPatId(1);
        historyDTO.setPatient("Doe");
        historyDTO.setCreationDate(LocalDate.now());
        historyDTO.setNotes("test");

        // WHEN
        // I update the infos
        when(historyService.updateHistory(any(String.class), any(HistoryDTO.class))).thenReturn(true);

        // THEN
        // it shouldn't update the history
        mvc.perform(MockMvcRequestBuilders.put("/patHistory/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(historyDTO)))
                .andExpect(status().isOk());

    }

    @Test
    void testDeleteHistory() throws Exception {
        // GIVEN
        String historyId = "1";

        // WHEN
        when(historyService.deleteHistory(any(String.class))).thenReturn(true);

        // THEN
        mvc.perform(delete("/patHistory/1")).andExpect(status().isOk());
    }

}
