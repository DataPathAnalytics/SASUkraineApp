package com.datapath.sasu.dao.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Region {

    @Id
    private Integer id;
    private String name;
    private String caseName;

}
