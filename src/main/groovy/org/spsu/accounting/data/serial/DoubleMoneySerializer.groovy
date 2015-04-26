package org.spsu.accounting.data.serial

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider

/**
 * Created by bpeel on 4/11/15.
 */
public class DoubleMoneySerializer extends JsonSerializer<Double> {
    @Override
    public void serialize(Double value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
            JsonProcessingException {
        // put your desired money style here
        //jgen.writeNumber()
        jgen.writeNumber(convertToString(new BigDecimal(value)));
    }

    public String convertToString(BigDecimal value){
        if (value == null)
            return null
        return value.setScale(2, BigDecimal.ROUND_HALF_UP).toString()
    }
}
