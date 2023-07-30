package com.mediscreen.history.service;

import com.mediscreen.history.Exception.HistoryNotFoundException;
import com.mediscreen.history.dto.HistoryDTO;
import com.mediscreen.history.model.History;
import com.mediscreen.history.model.MapstructMapper;
import com.mediscreen.history.repository.HistoryRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing patient history information.
 */
@Service
public class HistoryService { ;

    MapstructMapper mapper = Mappers.getMapper(MapstructMapper.class);
    @Autowired
    HistoryRepository historyRepository;

    /**
     * Retrieves a patient history by its ID.
     *
     * @param id The ID of the history to retrieve.
     * @return The patient history as a HistoryDTO object.
     * @throws HistoryNotFoundException if the history with the given ID is not found.
     */
    public HistoryDTO getHistoryById(String id) {
        Optional<History> history = historyRepository.findById(id);

        if (history.isPresent()) {
            HistoryDTO historyDTO = mapper.historyToDTO(history.get());
            return historyDTO;
        }

        throw new HistoryNotFoundException("History not found with ID: " + id);
    }

    /**
     * Retrieves all patient histories associated with a given patient ID.
     *
     * @param id The ID of the patient.
     * @return A list of patient histories as HistoryDTO objects.
     */
    public List<HistoryDTO> getHistoryByPatientId(Integer id) {
        List<History> historyList = historyRepository.findByPatId(id);

        if (!historyList.isEmpty()) {
            List<HistoryDTO> historyDTOList = historyList.stream()
                    .map(mapper::historyToDTO)
                    .collect(Collectors.toList());
            return historyDTOList;
        }

        return new ArrayList<>();
    }

    /**
     * Retrieves all patient histories.
     *
     * @return A list of all patient histories as HistoryDTO objects.
     * @throws HistoryNotFoundException if no patient histories are found.
     */
    public List<HistoryDTO> getAllHistories() {
        try {
            List<History> histories = historyRepository.findAll();
            return mapper.historyListToDTO(histories);
        } catch (Exception e) {
            throw new HistoryNotFoundException("Patient histories are not found.");
        }
    }

    /**
     * Adds a new patient history.
     *
     * @param historyDTO The HistoryDTO object containing the information of the new history.
     * @return true if the history is added successfully, false otherwise.
     */
    public boolean addHistory(HistoryDTO historyDTO) {
        try {
            History history = mapper.historyDTOtoEntity(historyDTO);
            history.setCreationDate(LocalDate.now());
            historyRepository.save(history);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates an existing patient history with the given ID.
     *
     * @param id         The ID of the history to update.
     * @param historyDTO The updated HistoryDTO object.
     * @return true if the history is updated successfully, false otherwise.
     * @throws HistoryNotFoundException if the history with the given ID is not found.
     */
    public boolean updateHistory(String id, HistoryDTO historyDTO) {
        Optional<History> optionalHistory = historyRepository.findById(id);

        if (optionalHistory.isPresent()) {
            History history = optionalHistory.get();
            historyDTO.setId(id);
            history = mapper.historyDTOtoEntity(historyDTO);
            historyRepository.save(history);
            return true;
        }

        throw new HistoryNotFoundException("Patient history not found with ID: " + id);
    }

    /**
     * Deletes a patient history by its ID.
     *
     * @param id The ID of the history to delete.
     * @return true if the history is deleted successfully, false otherwise.
     * @throws HistoryNotFoundException if the history with the given ID is not found.
     */
    public boolean deleteHistory(String id) {
        Optional<History> history = historyRepository.findById(id);

        if (history.isPresent()) {
            historyRepository.deleteById(id);
            return true;
        }

        throw new HistoryNotFoundException("Patient history not found with ID: " + id);
    }
}
