package com.datapath.sasu.dao.service;

import com.datapath.sasu.controllers.response.LocalMethod;
import com.datapath.sasu.controllers.response.RegionDTO;
import com.datapath.sasu.dao.repository.OfficeRepository;
import com.datapath.sasu.dao.repository.ReasonRepository;
import com.datapath.sasu.dao.repository.ViolationRepository;
import com.datapath.sasu.dao.response.MappingDAOResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MappingDAOService {

    private final OfficeRepository officeRepository;
    private final ViolationRepository violationRepository;
    private final ReasonRepository reasonRepository;

    @Transactional
    public MappingDAOResponse getResponse() {
        var response = new MappingDAOResponse();
        response.setOffices(officeRepository.findAll());
        response.setViolations(violationRepository.findAll());
        response.setReasons(reasonRepository.findAll());
        response.setLocalMethods(getLocalMethods());
        return response;
    }

    //TODO add new entity to database local_method and return data from it
    private List<LocalMethod> getLocalMethods() {
        List<LocalMethod> result = new ArrayList<>();
        result.add(new LocalMethod("aboveThresholdUA", "Відкриті торги"));
        result.add(new LocalMethod("aboveThresholdEU", "Відкриті торги з публікацією англійською мовою"));
        result.add(new LocalMethod("negotiation", "Переговорна процедура"));
        result.add(new LocalMethod("negotiation.quick", "Переговорна процедура (скорочена)"));
        return result;
    }

}
