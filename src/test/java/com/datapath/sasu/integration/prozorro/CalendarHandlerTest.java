package com.datapath.sasu.integration.prozorro;

import com.datapath.sasu.integration.prozorro.calendar.CalendarHandler;
import com.datapath.sasu.integration.prozorro.calendar.CalendarReceiver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class CalendarHandlerTest {

    @Test
    void testWorkDaysCount() {
        var receiver = mock(CalendarReceiver.class);
        var handler = new CalendarHandler(receiver);

        var weekendsOn = List.of(
                LocalDate.of(2021, 4, 4)
        );
        var workdaysOff = List.of(
                LocalDate.of(2021, 4, 7)
        );


        doReturn(weekendsOn).when(receiver).getWeekendsOn();
        doReturn(workdaysOff).when(receiver).getWorkdaysOff();

        var startDate = LocalDate.of(2021, 4, 1);
        var endDate = LocalDate.of(2021, 4, 12);

        long workDaysCount = handler.getWorkDaysCount(startDate, endDate);
        Assertions.assertEquals(7, workDaysCount);
    }
}
