package org.landon.serialization.deserializers;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import org.landon.editor.windows.logger.Logger;
import org.landon.serialization.types.LeapEnum;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class EnumDeserializer implements ObjectReader<LeapEnum<?>> {

    @Override
    public LeapEnum<?> readObject(JSONReader jsonReader, Type fieldType, Object fieldName, long features) {
        JSONObject jsonObject = jsonReader.readJSONObject();
        String typeName = jsonObject.getString("type");
        Class<? extends Enum> typeClass = null;
        try {
            typeClass = (Class<? extends Enum>) Class.forName(typeName);
        } catch (ClassNotFoundException e) {
            Logger.error(e);
        }

        if (typeClass == null) {
            return null;
        }

        Enum value = Enum.valueOf(typeClass, jsonObject.getString("value"));
        return new LeapEnum<>(value);
    }

}
