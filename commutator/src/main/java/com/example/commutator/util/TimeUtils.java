package com.example.commutator.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneOffset;

/**
 * This class provides various utility methods for handling time-related operations.
 */
@Component
public class TimeUtils {

    private final static ZoneOffset OFFSET = ZoneOffset.UTC;

    /**
     * Calculates the Unix time at the start of the specified month.
     *
     * @param date the date for which the Unix time is calculated
     * @return the Unix time at the start of the specified date
     */
    public long getStartOfMonthUnixTime(LocalDate date) {
        return date.atStartOfDay()
                .toInstant(OFFSET).toEpochMilli() / 1000;
    }

    /**
     * Calculates the Unix time at the end of the specified month.
     *
     * @param date the date for which the Unix time is calculated
     * @return the Unix time at the end of the specified date
     */
    public long getEndOfMonthUnixTime(LocalDate date) {
        return date.plusMonths(1)
                .atStartOfDay().minusSeconds(1)
                .toInstant(OFFSET).toEpochMilli() / 1000;
    }

}
