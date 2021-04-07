package com.datapath.sasu;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static java.time.LocalTime.MIDNIGHT;

@UtilityClass
public class Constants {

    public static final ZoneId UA_ZONE = ZoneId.of("Europe/Kiev");
    public static final ZonedDateTime MONITORING_START = ZonedDateTime.of(LocalDate.of(2019, 1, 1), MIDNIGHT, UA_ZONE);
    public static final String ABOVE_THRESHOLD_UA = "aboveThresholdUA";
    public static final String ABOVE_THRESHOLD_EU = "aboveThresholdEU";
    public static final String NEGOTIATION = "negotiation";
    public static final String NEGOTIATION_QUICK = "negotiation.quick";

    public static final List<String> OPEN_METHOD_TYPES = List.of(ABOVE_THRESHOLD_UA, ABOVE_THRESHOLD_EU);
    public static final List<String> LIMITED_METHOD_TYPES = List.of(NEGOTIATION, NEGOTIATION_QUICK);

}
