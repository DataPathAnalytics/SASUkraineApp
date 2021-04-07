package com.datapath.sasu.dao.repository;

import com.datapath.sasu.dao.entity.Monitoring;

import java.util.Optional;

public interface MonitoringRepository extends OuterRepository<Monitoring, Integer> {

    Optional<Monitoring> findFirstByOrderByDateModifiedDesc();

}
