package org.spsu.accounting.data.serial

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.joda.time.DateTime

import java.text.SimpleDateFormat

/**
 * Created by bpeel on 4/11/15.
 */
public class DateTimeSerializer extends JsonSerializer<DateTime> {

    final static String pattern = "MM/dd/yyyy hh:mm:ss"
    @Override
    public void serialize(DateTime value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
            JsonProcessingException {
        // put your desired money style here
        jgen.writeString(convertToString(value));
    }

    public String convertToString(DateTime value){
        return value.toString(pattern)
    }
}
