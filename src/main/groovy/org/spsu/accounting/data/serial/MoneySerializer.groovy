package org.spsu.accounting.data.serial

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider

/**
 * Created by bpeel on 4/11/15.
 */
public class MoneySerializer extends JsonSerializer<BigDecimal> {
    @Override
    public void serialize(BigDecimal value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
            JsonProcessingException {
        // put your desired money style here
        jgen.writeString(convertToString(value));
    }

    public String convertToString(BigDecimal value){
        if (value == null)
            return null

       return String.format("%,.2f", value.setScale(2, BigDecimal.ROUND_HALF_UP));
       // return value.setScale(2, BigDecimal.ROUND_HALF_UP).toString()
    }
}
