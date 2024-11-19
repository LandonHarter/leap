package org.landon.serialization.serializers;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;
import org.landon.serialization.types.LeapEnum;

import java.lang.reflect.Type;

public class EnumSerializer implements ObjectWriter<LeapEnum<?>> {

    @Override
    public void write(JSONWriter jsonWriter, Object object, Object fieldName, Type fieldType, long features) {
        LeapEnum<?> leapEnum = (LeapEnum<?>) object;

        jsonWriter.startObject();
        jsonWriter.writeName("type");
        jsonWriter.writeColon();
        jsonWriter.writeString(leapEnum.getValue().getClass().getName());
        jsonWriter.writeName("value");
        jsonWriter.writeColon();
        jsonWriter.writeString(leapEnum.getValue().toString());
        jsonWriter.endObject();
    }

}
