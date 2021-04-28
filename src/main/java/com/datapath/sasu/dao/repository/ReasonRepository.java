package com.datapath.sasu.dao.repository;

import com.datapath.sasu.dao.entity.Reason;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReasonRepository extends JpaRepository<Reason, Integer> {

    Optional<Reason> findByName(String name);

}
