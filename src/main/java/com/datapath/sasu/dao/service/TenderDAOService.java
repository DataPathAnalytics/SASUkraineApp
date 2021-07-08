package com.datapath.sasu.dao.service;

import com.datapath.sasu.dao.entity.Tender;

import java.util.Optional;

public interface TenderDAOService {

    Optional<Tender> getLastModifiedTender();

    Integer save(Tender tender);

}
