package org.landon.serialization.serializers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import org.landon.scene.GameObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

public class GameObjectSerializer implements ObjectSerializer {

    @Override
    public void write(JSONSerializer serializer, Object o, Object o1, Type type, int i) throws IOException {
        GameObject object = (GameObject) o;

        System.out.println("Serializing game object: " + object);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", object.getName());
        jsonObject.put("transform", object.getTransform());

        JSONArray components = new JSONArray();
        object.getComponents().forEach(component -> {
            JSONObject componentObject = new JSONObject();
            componentObject.put("type", component.getClass().getName());

            for (Field field : component.getClass().getDeclaredFields()) {
                if (Modifier.isTransient(field.getModifiers())) continue;
                field.setAccessible(true);
                try {
                    componentObject.put(field.getName(), field.get(component));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            components.add(componentObject);
        });
        jsonObject.put("components", components);

        serializer.write(jsonObject);
    }

}
