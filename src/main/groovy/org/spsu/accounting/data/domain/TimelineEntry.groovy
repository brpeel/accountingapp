package org.spsu.accounting.data.domain

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.joda.time.DateTime
import org.spsu.accounting.data.serial.DateElapsedSerializer
import org.spsu.accounting.data.serial.DateSerializer
import org.spsu.accounting.data.serial.DateTimeSerializer

/**
 * Created by bpeel on 4/26/15.
 */
class TimelineEntry {

    String username

    String fullname

    String msg

    @JsonSerialize(using = DateElapsedSerializer)
    DateTime eventtime

    @JsonSerialize(using = DateTimeSerializer)
    DateTime eventhour


}
