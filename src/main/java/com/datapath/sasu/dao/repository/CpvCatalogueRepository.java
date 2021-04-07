package com.datapath.sasu.dao.repository;

import com.datapath.sasu.dao.entity.CpvCatalogue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CpvCatalogueRepository extends JpaRepository<CpvCatalogue, Integer> {

}
