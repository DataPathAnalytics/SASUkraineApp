package com.datapath.sasu.dao.repository;

import com.datapath.sasu.dao.entity.ProcurementCategory;
import org.springframework.data.repository.CrudRepository;

public interface ProcuringCategoryRepository extends CrudRepository<ProcurementCategory, Integer> {

    ProcurementCategory findByNameEn(String name);

}
