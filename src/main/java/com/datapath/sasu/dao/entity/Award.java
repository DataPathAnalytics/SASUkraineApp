package com.datapath.sasu.dao.entity;

import lombok.Data;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity(name = "award")
public class Award {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    private String outerId;

    private Double value;

    private String status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tender_id")
    private Tender tender;

}
