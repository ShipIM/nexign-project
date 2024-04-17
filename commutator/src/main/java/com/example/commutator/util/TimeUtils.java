package com.example.commutator.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneOffset;

@Component
public class TimeUtils {

    private final static ZoneOffset OFFSET = ZoneOffset.UTC;

    public long getStartOfMonthUnixTime(LocalDate date) {
        return date.atStartOfDay()
                .toInstant(OFFSET).toEpochMilli() / 1000;
    }

    public long getEndOfMonthUnixTime(LocalDate date) {
        return date.plusMonths(1)
                .atStartOfDay().minusSeconds(1)
                .toInstant(OFFSET).toEpochMilli() / 1000;
    }

}
