package org.landon.serialization.deserializers;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.reader.ObjectReader;
import org.landon.components.Component;
import org.landon.editor.windows.logger.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.math.BigDecimal;

public class ComponentDeserializer implements ObjectReader<Component> {

    @Override
    public Component readObject(JSONReader jsonReader, Type fieldType, Object fieldName, long features) {
        JSONObject jsonObject = jsonReader.readJSONObject();

        String typeName = jsonObject.getString("type");
        Class<?> typeClass = null;
        try {
            typeClass = Class.forName(typeName);

            if (!Component.class.isAssignableFrom(typeClass)) {
                Logger.error("Type " + typeName + " is not a component");
                return null;
            }
        } catch (ClassNotFoundException e) {
            Logger.error(e);
        }

        JSONObject properties = jsonObject.getJSONObject("properties");
        Component component = null;
        try {
            component = (Component) typeClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            Logger.error(e);
        }

        if (component == null) {
            return null;
        }

        Field[] fields = typeClass.getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            field.setAccessible(true);
            String fieldNameStr = field.getName();
            if (!properties.containsKey(fieldNameStr)) {
                continue;
            }

            Object value = JSON.parseObject(properties.getString(fieldNameStr), field.getType());
            if (value == null) {
                continue;
            }

            try {
                field.set(component, value);
            } catch (IllegalAccessException e) {
                Logger.error(e);
            }
        }

        component.load();
        return component;
    }

}
