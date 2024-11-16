package org.landon.serialization.deserializers;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import org.landon.components.Component;

import java.lang.reflect.Type;

public class ComponentDeserializer implements ObjectReader<Component> {

    @Override
    public Component readObject(JSONReader jsonReader, Type fieldType, Object fieldName, long features) {
        JSONObject jsonObject = jsonReader.readJSONObject();

        String typeName = jsonObject.getString("type");
        Class<?> typeClass = null;
        try {
            typeClass = Class.forName(typeName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        JSONObject properties = jsonObject.getJSONObject("properties");
        return (Component) JSON.parseObject(properties.toString(), typeClass);
    }

}
