package org.landon.serialization.serializers;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;
import org.landon.math.Transform;
import org.landon.scene.GameObject;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

public class GameObjectSerializer implements ObjectWriter<GameObject> {

    @Override
    public void write(JSONWriter jsonWriter, Object object, Object fieldName, Type fieldType, long features) {
        GameObject gameObject = (GameObject) object;

//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("uuid", gameObject.getUuid());
//        jsonObject.put("name", gameObject.getName());
//        jsonObject.put("enabled", gameObject.isEnabled());
//        jsonObject.put("transform", gameObject.getTransform());
//        jsonObject.put("parent", gameObject.getParent() != null ? gameObject.getParent().getUuid() : null);
//
//        JSONArray components = new JSONArray();
//        gameObject.getComponents().forEach(component -> {
//            JSONObject componentObject = new JSONObject();
//            componentObject.put("type", component.getClass().getName());
//
//            JSONObject properties = new JSONObject();
//            for (Field field : component.getClass().getDeclaredFields()) {
//                if (Modifier.isTransient(field.getModifiers())) continue;
//                field.setAccessible(true);
//                try {
//                    properties.put(field.getName(), field.get(component));
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            componentObject.put("properties", properties);
//            components.add(componentObject);
//        });
//        jsonObject.put("components", components);

//        jsonWriter.write(jsonObject);

        jsonWriter.startObject();
        jsonWriter.writeName("uuid");
        jsonWriter.writeColon();
        jsonWriter.writeString(gameObject.getUuid());
        jsonWriter.writeName("name");
        jsonWriter.writeColon();
        jsonWriter.writeString(gameObject.getName());
        jsonWriter.writeName("enabled");
        jsonWriter.writeColon();
        jsonWriter.writeBool(gameObject.isEnabled());
        jsonWriter.writeName("transform");
        jsonWriter.writeColon();
        jsonWriter.writeAs(gameObject.getTransform(), Transform.class);
        jsonWriter.writeName("parent");
        jsonWriter.writeColon();
        jsonWriter.writeString(gameObject.getParent() != null ? gameObject.getParent().getUuid() : null);
        jsonWriter.writeName("components");
        jsonWriter.writeColon();
        jsonWriter.startArray();
        gameObject.getComponents().forEach(component -> {
            jsonWriter.startObject();

            jsonWriter.writeName("type");
            jsonWriter.writeColon();
            jsonWriter.writeString(component.getClass().getName());

            jsonWriter.writeName("properties");
            jsonWriter.writeColon();
            jsonWriter.startObject();
            for (Field field : component.getClass().getDeclaredFields()) {
                if (Modifier.isTransient(field.getModifiers())) continue;
                field.setAccessible(true);
                try {
                    jsonWriter.writeName(field.getName());
                    jsonWriter.writeColon();
                    jsonWriter.writeAs(field.get(component), field.getType());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            jsonWriter.endObject();
            jsonWriter.endObject();
            jsonWriter.writeComma();
        });
        jsonWriter.endArray();
        jsonWriter.endObject();
    }

}
