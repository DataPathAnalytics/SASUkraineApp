package com.datapath.sasu.integration.prozorro.tendering.containers;

import com.datapath.sasu.integration.prozorro.containers.Page;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TendersAPIResponse {
    private List<TenderAPI> data;
    @JsonProperty("next_page")
    private Page nextPage;
//    @JsonProperty("prev_page")
//    private Page prevPage;
}
