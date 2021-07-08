package com.datapath.sasu.dao.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.LAZY;

@Entity(name = "tender")
@Data
public class Tender {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String hash;
    private String outerId;
    private LocalDateTime dateModified;
    private LocalDateTime date;
    private String status;
    private String localMethod;

    private Double expectedValue;
    private Double value;

    private LocalDate startDate;
    private boolean competitive;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "procurement_category_id")
    private ProcurementCategory procurementCategory;

    @ManyToOne(cascade = PERSIST, optional = false, fetch = LAZY)
    @JoinColumn(name = "procuring_entity_id")
    private ProcuringEntity procuringEntity;

    @OneToMany(mappedBy = "tender", cascade = ALL, orphanRemoval = true)
    private List<TenderItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "tender", cascade = ALL, orphanRemoval = true)
    private List<Award> awards = new ArrayList<>();

    public void addAward(Award award) {
        awards.add(award);
        award.setTender(this);
    }

    public void addItem(TenderItem item) {
        items.add(item);
        item.setTender(this);
    }

    public void clearAwards() {
        awards.clear();
    }

}
