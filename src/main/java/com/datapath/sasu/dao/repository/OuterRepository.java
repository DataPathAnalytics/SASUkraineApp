package com.datapath.sasu.dao.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface OuterRepository<T, ID> extends CrudRepository<T, ID> {

    Optional<T> findByOuterId(String outerId);

}
