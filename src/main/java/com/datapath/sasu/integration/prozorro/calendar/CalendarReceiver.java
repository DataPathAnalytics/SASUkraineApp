package com.datapath.sasu.integration.prozorro.calendar;

import java.time.LocalDate;
import java.util.List;

public interface CalendarReceiver {

    List<LocalDate> getWeekendsOn();

    List<LocalDate> getWorkdaysOff();

}
