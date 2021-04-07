package com.datapath.sasu.dao.entity;

import lombok.Data;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Data
@Entity(name = "tender_item")
public class TenderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String outerId;

    @ManyToOne(fetch = LAZY,optional = false)
    @JoinColumn(name = "cpv_id")
    private CpvCatalogue cpv;

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "tender_id")
    private Tender tender;

}
