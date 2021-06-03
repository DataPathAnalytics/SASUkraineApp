package com.datapath.sasu.integration.prozorro.calendar;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;

@Component
@AllArgsConstructor
public class CalendarHandler {

    private static final List<DayOfWeek> WEEKENDS_DAYS = List.of(SATURDAY, SUNDAY);
    private CalendarReceiver receiver;

    public int getWorkDaysCount(LocalDate start, LocalDate end) {
        List<LocalDate> workdaysOff = receiver.getWorkdaysOff();
        List<LocalDate> weekendsOn = receiver.getWeekendsOn();

        List<LocalDate> dates = start.datesUntil(end)
                .filter(date -> {
                    if (WEEKENDS_DAYS.contains(date.getDayOfWeek())) {
                        return weekendsOn.contains(date);
                    } else {
                        return true;
                    }
                })
                .filter(date -> !workdaysOff.contains(date))
                .collect(Collectors.toList());

        return dates.size();
    }

}
