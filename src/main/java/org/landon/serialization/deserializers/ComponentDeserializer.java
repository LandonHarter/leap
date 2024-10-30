package org.landon.serialization.deserializers;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import java.lang.reflect.Type;

public class ComponentDeserializer implements ObjectDeserializer {

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object o) {
        JSONObject jsonObject = parser.parseObject();

        String typeName = jsonObject.getString("type");
        Class<?> typeClass = null;
        try {
            typeClass = Class.forName(typeName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Object component = jsonObject.getObject("properties", typeClass);
        return (T) component;
    }

}
