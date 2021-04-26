package com.datapath.sasu.dao.repository;

import com.datapath.sasu.dao.entity.Violation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ViolationRepository extends JpaRepository<Violation, Integer> {

    Optional<Violation> findByName(String name);

}
