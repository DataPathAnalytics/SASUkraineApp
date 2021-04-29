package com.datapath.sasu.dao.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@NoArgsConstructor
public class Reason {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    private String name;
    private String nameUa;

    public Reason(String name) {
        this.name = name;
        nameUa = name;
    }

}
