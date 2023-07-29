package com.mediscreen.history.serviceTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mediscreen.history.Exception.HistoryNotFoundException;
import com.mediscreen.history.dto.HistoryDTO;
import com.mediscreen.history.model.History;
import com.mediscreen.history.repository.HistoryRepository;
import com.mediscreen.history.service.HistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class HistoryServiceTest {

    @Mock
    private HistoryRepository historyRepository;

    @InjectMocks
    private HistoryService historyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetHistoryById_ValidId_ReturnsHistoryDTO() {
        // GIVEN
        String historyId = "1";
        History history = new History();
        history.setId(historyId);
        when(historyRepository.findById(historyId)).thenReturn(Optional.of(history));

        // WHEN
        HistoryDTO result = historyService.getHistoryById(historyId);

        // THEN
        assertNotNull(result);
        assertEquals(historyId, result.getId());
    }

    @Test
    void testGetHistoryById_InvalidId_ThrowsHistoryNotFoundException() {
        // GIVEN
        String historyId = "nonexistent";
        when(historyRepository.findById(historyId)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(HistoryNotFoundException.class, () -> historyService.getHistoryById(historyId));
    }

    @Test
    void testGetHistoryByPatientId_ValidPatientId_ReturnsListOfHistoryDTOs() {
        // GIVEN
        Integer patientId = 1;
        History history = new History();
        history.setId("1");
        history.setPatId(patientId);
        List<History> historyList = List.of(history);
        when(historyRepository.findByPatId(patientId)).thenReturn(historyList);

        // WHEN
        List<HistoryDTO> result = historyService.getHistoryByPatientId(patientId);

        // THEN
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("1", result.get(0).getId());
        assertEquals(patientId, result.get(0).getPatId());
    }

    @Test
    void testGetHistoryByPatientId_InvalidPatientId_ReturnsEmptyList() {
        // GIVEN
        Integer patientId = 123;
        when(historyRepository.findByPatId(patientId)).thenReturn(new ArrayList<>());

        // WHEN
        List<HistoryDTO> result = historyService.getHistoryByPatientId(patientId);

        // THEN
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllHistories_ReturnsListOfHistoryDTOs() {
        // GIVEN
        History history1 = new History();
        history1.setId("1");
        History history2 = new History();
        history2.setId("2");
        List<History> histories = List.of(history1, history2);
        when(historyRepository.findAll()).thenReturn(histories);

        // WHEN
        List<HistoryDTO> result = historyService.getAllHistories();

        // THEN
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
    }

    @Test
    void testAddHistory_ValidData_ReturnsTrue() {
        // GIVEN
        HistoryDTO historyDTO = new HistoryDTO();
        historyDTO.setId("1");
        when(historyRepository.save(any(History.class))).thenReturn(new History());

        // WHEN
        boolean result = historyService.addHistory(historyDTO);

        // THEN
        assertTrue(result);
    }

    @Test
    void testUpdateHistory_ExistingHistory_ReturnsTrue() {
        // GIVEN
        String historyId = "1";
        History history = new History();
        history.setId(historyId);
        when(historyRepository.findById(historyId)).thenReturn(Optional.of(history));
        when(historyRepository.save(any(History.class))).thenReturn(new History());

        // WHEN
        boolean result = historyService.updateHistory(historyId, new HistoryDTO());

        // THEN
        assertTrue(result);
    }

    @Test
    void testUpdateHistory_NonexistentHistory_ThrowsHistoryNotFoundException() {
        // GIVEN
        String historyId = "nonexistent";
        when(historyRepository.findById(historyId)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(HistoryNotFoundException.class, () -> historyService.updateHistory(historyId, new HistoryDTO()));
    }

    @Test
    void testDeleteHistory_ExistingHistory_ReturnsTrue() {
        // GIVEN
        String historyId = "1";
        when(historyRepository.findById(historyId)).thenReturn(Optional.of(new History()));

        // WHEN
        boolean result = historyService.deleteHistory(historyId);

        // THEN
        assertTrue(result);
        verify(historyRepository, times(1)).deleteById(historyId);
    }

    @Test
    void testDeleteHistory_NonexistentHistory_ThrowsHistoryNotFoundException() {
        // GIVEN
        String historyId = "nonexistent";
        when(historyRepository.findById(historyId)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(HistoryNotFoundException.class, () -> historyService.deleteHistory(historyId));
    }
}

