package com.datapath.sasu.controllers.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SupportEmailRequest {

    @NotNull
    private String from;
    private String subject;
    @NotNull
    private String text;

}
