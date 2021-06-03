package com.datapath.sasu.integration.prozorro.calendar;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

@Component
@AllArgsConstructor
public class CalendarReceiverImpl implements CalendarReceiver {

    public static final String WEEKENDS_ON = "https://prozorroukr.github.io/standards/calendars/weekends_on.json";
    public static final String WORKDAYS_OFF = "https://prozorroukr.github.io/standards/calendars/workdays_off.json";

    private final RestTemplate restTemplate;

    public List<LocalDate> getWeekendsOn() {
        LocalDate[] dates = restTemplate.getForObject(WEEKENDS_ON, LocalDate[].class);
        return List.of(dates);
    }

    public List<LocalDate> getWorkdaysOff() {
        LocalDate[] dates = restTemplate.getForObject(WORKDAYS_OFF, LocalDate[].class);
        return List.of(dates);
    }

}
