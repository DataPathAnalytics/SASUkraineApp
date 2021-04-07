package com.datapath.sasu.integration.prozorro.monitoring.containers;

import com.datapath.sasu.integration.prozorro.containers.AddressAPI;
import com.datapath.sasu.integration.prozorro.containers.ContactPointAPI;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class Party {

    private String name;
    private ZonedDateTime datePublished;
    private ContactPointAPI contactPoint;
    private AddressAPI address;

}
