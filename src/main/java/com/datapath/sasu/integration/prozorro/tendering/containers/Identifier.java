package com.datapath.sasu.integration.prozorro.tendering.containers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Identifier {

    private String id;
    private String scheme;
    private String legalName;

}
