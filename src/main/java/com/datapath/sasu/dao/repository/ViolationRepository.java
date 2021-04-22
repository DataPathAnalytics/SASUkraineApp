package com.datapath.sasu.dao.repository;

import com.datapath.sasu.dao.entity.Violation;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ViolationRepository extends CrudRepository<Violation, Integer> {

    Optional<Violation> findByName(String name);

    @Override
    List<Violation> findAll();

}
