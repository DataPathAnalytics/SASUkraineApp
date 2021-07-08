package com.datapath.sasu.dao.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
public class Monitoring {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    private String outerId;
    private LocalDateTime dateModified;
    private String result;

    private LocalDateTime startDate;
    private String startMonth;

    private LocalDateTime endDate;
    private String endMonth;

    private Integer duration;
    private boolean concluded;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tender_id")
    private Tender tender;

    @ManyToOne(cascade = PERSIST)
    @JoinColumn(name = "auditor_id")
    private Auditor auditor;

    @ManyToOne(cascade = PERSIST)
    @JoinColumn(name = "sasu_office_id")
    private Office office;


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "monitoring_violation",
            joinColumns = @JoinColumn(name = "monitoring_id"),
            inverseJoinColumns = @JoinColumn(name = "violation_id"))
    private List<Violation> violations = new ArrayList<>();

    @ManyToMany(cascade = { MERGE, PERSIST })
    @JoinTable(
            name = "monitoring_reason",
            joinColumns = @JoinColumn(name = "monitoring_id"),
            inverseJoinColumns = @JoinColumn(name = "reason_id"))
    private List<Reason> reasons = new ArrayList<>();

}
