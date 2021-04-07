package com.datapath.sasu.dao.repository;

import com.datapath.sasu.dao.entity.Region;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RegionRepository extends CrudRepository<Region, Integer> {

    @Query(nativeQuery = true, value = "SELECT * FROM region WHERE ?1 = ANY(aliases)")
    Region findRegionByAlias(String alias);

}
