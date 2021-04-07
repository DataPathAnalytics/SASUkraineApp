package com.datapath.sasu.dao.repository;

import com.datapath.sasu.dao.entity.TenderItem;

import java.util.Optional;

public interface TenderItemRepository extends OuterRepository<TenderItem, Integer> {

    Optional<TenderItem> findByOuterIdAndTenderId(String outerId, Integer tenderId);

}
