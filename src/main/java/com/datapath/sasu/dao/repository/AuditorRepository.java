package com.datapath.sasu.dao.repository;

import com.datapath.sasu.dao.entity.Auditor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuditorRepository extends JpaRepository<Auditor, Integer> {
    Optional<Auditor> findByEmail(String email);
}
