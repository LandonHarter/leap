package org.landon.serialization;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.filter.PropertyPreFilter;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class TransientFilter implements PropertyPreFilter {

    @Override
    public boolean process(JSONWriter writer, Object source, String name) {
        try {
            Field field = source.getClass().getDeclaredField(name);
            return !Modifier.isTransient(field.getModifiers());
        } catch (NoSuchFieldException e) {
            return false;
        }
    }

}
