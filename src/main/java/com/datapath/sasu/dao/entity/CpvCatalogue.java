package com.datapath.sasu.dao.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class CpvCatalogue {

    @Id
    private Integer id;
    private String cpvCode;
    private String cpv;
    private String cpv2;
    private String cpv3;
    private String cpv4;
}
