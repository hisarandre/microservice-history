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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HistoryService { ;

    MapstructMapper mapper = Mappers.getMapper(MapstructMapper.class);
    @Autowired
    HistoryRepository historyRepository;

    public HistoryDTO getHistoryById(String id) {
        Optional<History> history = historyRepository.findById(id);

        if (history.isPresent()) {
            HistoryDTO historyDTO = mapper.historyToDTO(history.get());
            return historyDTO;
        }

        throw new HistoryNotFoundException("History not found with ID: " + id);
    }

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


    public List<HistoryDTO> getAllHistories() {
        try {
            List<History> histories = historyRepository.findAll();
            return mapper.historyListToDTO(histories);
        } catch (Exception e) {
            throw new HistoryNotFoundException("Patient histories are not found.");
        }
    }

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

    public boolean deleteHistory(String id) {
        Optional<History> history = historyRepository.findById(id);

        if (history.isPresent()) {
            historyRepository.deleteById(id);
            return true;
        }

        throw new HistoryNotFoundException("Patient history not found with ID: " + id);
    }
}
