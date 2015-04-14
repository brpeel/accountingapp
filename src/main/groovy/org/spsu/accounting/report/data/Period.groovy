package org.spsu.accounting.report.data

import org.joda.time.DateTime

import java.sql.Timestamp

/**
 * Created by bpeel on 4/13/15.
 */
class Period {

    public final DateTime start
    public final DateTime end

    public final Timestamp startTime
    public final Timestamp endTime

    public Period(int year, int month){
        start = new DateTime(year, month, 1, 0, 0)
        end = start.plusMonths(1)

        startTime = new Timestamp(start.millis)
        endTime = new Timestamp(end.millis)
    }

    public Period(int year, int startMonth, int endMonth){
        this(year, startMonth, year, endMonth)
    }

    public Period(int startYear, int startMonth, int endYear, int endMonth){
        start = new DateTime(startYear, startMonth, 1, 0, 0)
        end = new DateTime(endYear, endMonth, 1, 0, 0)

        startTime = new Timestamp(start.millis)
        endTime = new Timestamp(end.millis)
    }

}
