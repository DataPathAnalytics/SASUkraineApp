package com.datapath.sasu.dao.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity(name = "procurement_category")
@NoArgsConstructor
public class ProcurementCategory {

    @Id
    private Integer id;
    private String nameEn;
    private String nameUa;

}
