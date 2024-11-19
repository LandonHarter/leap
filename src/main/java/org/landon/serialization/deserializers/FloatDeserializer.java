package org.landon.serialization.deserializers;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import org.landon.serialization.types.LeapFloat;

import java.lang.reflect.Type;

public class FloatDeserializer implements ObjectReader<LeapFloat> {

    @Override
    public LeapFloat readObject(JSONReader jsonReader, Type fieldType, Object fieldName, long features) {
        JSONObject jsonObject = jsonReader.readJSONObject();
        return new LeapFloat(jsonObject.getFloatValue("value"));
    }

}
