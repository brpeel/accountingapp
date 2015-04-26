package org.spsu.accounting.data.serial

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.joda.time.DateTime

/**
 * Created by bpeel on 4/11/15.
 */
public class DateElapsedSerializer extends JsonSerializer<DateTime> {

    final static long ONE_MINUTE_OFFSET = 60 * 1000;
    final static long FIVE_MINUTE_OFFSET = 5 * ONE_MINUTE_OFFSET;
    final static long THRITY_MINUTE_OFFSET = 30 * ONE_MINUTE_OFFSET;
    final static long ONE_HOUR_OFFSET = 60 * ONE_MINUTE_OFFSET;
    final static long THREE_HOUR_OFFSET = 3 * ONE_HOUR_OFFSET;

    final static String pattern = "MM/dd/yyyy hh:mm:ss"
    @Override
    public void serialize(DateTime value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
            JsonProcessingException {
        // put your desired money style here
        jgen.writeString(convertToString(value));
    }

    public String convertToString(DateTime value){
        long currentTime = System.currentTimeMillis()

        long time = value.millis

        if (time + ONE_MINUTE_OFFSET > currentTime)
            return "less than 1 minute ago"

        if (time + FIVE_MINUTE_OFFSET > currentTime)
            return "less than 5 minutes ago"

        if (time + THRITY_MINUTE_OFFSET > currentTime)
            return "less than 30 minutes ago"

        if (time + ONE_HOUR_OFFSET > currentTime)
            return "1 hour ago"

        if (time + THREE_HOUR_OFFSET > currentTime)
            return "2 hours ago"

        return value.toString(pattern)
    }
}
