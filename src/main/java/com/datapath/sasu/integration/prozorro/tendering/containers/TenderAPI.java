package com.datapath.sasu.integration.prozorro.tendering.containers;

import com.datapath.sasu.integration.prozorro.containers.Period;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;


//TODO check using LocalDateTime instead of ZonedDateTime maybe it default converts according to default locale
@Data
public class TenderAPI {

    private String id;
    private ZonedDateTime dateModified;
    private ZonedDateTime date;
    private String tenderID;
    private String mainProcurementCategory;
    private String status;
    private String procurementMethodType;
    private Period tenderPeriod;

    private List<TenderItemAPI> items;
    private Value value;
    private ProcuringEntityAPI procuringEntity;

    private List<AwardAPI> awards;

}
