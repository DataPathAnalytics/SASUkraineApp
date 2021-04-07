package com.datapath.sasu.dao.repository;

import com.datapath.sasu.dao.entity.Tender;

import java.util.Optional;

public interface TenderRepository extends OuterRepository<Tender, Integer> {

    Optional<Tender> findByHash(String hash);

    Optional<Tender> findFirstByOrderByDateModifiedDesc();

}
