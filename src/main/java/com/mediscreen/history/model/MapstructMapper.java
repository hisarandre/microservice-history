package com.mediscreen.history.model;

import com.mediscreen.history.dto.HistoryDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface MapstructMapper {

    List<HistoryDTO> historyListToDTO(List<History> histories);
    HistoryDTO historyToDTO(History History);
    History historyDTOtoEntity(HistoryDTO history);
}
