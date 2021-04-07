package com.datapath.sasu.integration.prozorro.tendering.containers;

import com.datapath.sasu.integration.prozorro.containers.AddressAPI;
import lombok.Data;

@Data
public class ProcuringEntityAPI {

    private AddressAPI address;
    private Identifier identifier;

}
