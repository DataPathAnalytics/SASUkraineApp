package com.datapath.sasu.dao.service;

import com.datapath.sasu.dao.repository.OfficeRepository;
import com.datapath.sasu.dao.repository.ReasonRepository;
import com.datapath.sasu.dao.repository.ViolationRepository;
import com.datapath.sasu.dao.response.MappingDAOResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return response;
    }

}
