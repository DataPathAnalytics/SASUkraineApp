package com.datapath.sasu.dao.entity;

import lombok.Data;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Table(name = "sasu_office")
public class Office {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "region_id")
    private Region region;

}
