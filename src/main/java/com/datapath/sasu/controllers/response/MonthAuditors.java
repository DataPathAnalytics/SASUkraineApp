package com.datapath.sasu.controllers.response;

import lombok.Data;

@Data
public class MonthAuditors {

    private Integer regionId;
    private String date;
    private Integer auditorsCount;

}
