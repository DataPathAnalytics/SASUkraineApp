package com.datapath.sasu.dao.service;

import com.datapath.sasu.dao.entity.Tender;
import com.datapath.sasu.dao.repository.TenderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class TenderDAOServiceImpl implements TenderDAOService {

    private final TenderRepository repository;

    public Optional<Tender> getLastModifiedTender() {
        return repository.findFirstByOrderByDateModifiedDesc();
    }

    @Override
    public Integer save(Tender tender) {
        return repository.save(tender).getId();
    }

}
