package org.spsu.accounting.report.data

import com.fasterxml.jackson.annotation.JsonGetter
import org.joda.time.DateTime

/**
 * Created by bpeel on 4/30/15.
 */
abstract class Statement {

    @JsonGetter("date")
    public String getDate(){
        DateTime now = new DateTime();

        String month = new java.text.DateFormatSymbols().months[ now.monthOfYear ]

        return "${month} ${now.dayOfMonth}, ${now.year}"

    }

    @JsonGetter("periodEnd")
    public String getPeriodEnd(){
        DateTime now = new DateTime();

        String day
        Calendar.with {
            day = instance.getActualMaximum( DATE )
        }

        String month = new java.text.DateFormatSymbols().months[ now.monthOfYear]

        return "${month} ${now.dayOfMonth}, ${now.year}"

    }
}
