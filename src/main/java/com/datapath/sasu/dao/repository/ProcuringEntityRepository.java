package com.datapath.sasu.dao.repository;

import com.datapath.sasu.dao.entity.ProcuringEntity;

import java.util.Optional;

public interface ProcuringEntityRepository extends OuterRepository<ProcuringEntity, Integer> {

    Optional<ProcuringEntity> findByOuterId(String outerId);

}
