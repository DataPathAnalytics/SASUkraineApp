package com.datapath.sasu.dao.repository;

import com.datapath.sasu.dao.entity.Office;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OfficeRepository extends JpaRepository<Office, Integer> {
    Optional<Office> findByName(String name);
    Optional<Office> findByRegionId(Integer regionId);


}
