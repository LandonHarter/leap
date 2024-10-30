package org.landon.serialization.deserializers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import org.landon.scene.GameObject;
import org.landon.scene.Scene;

import java.lang.reflect.Type;
import java.util.HashMap;

public class SceneDeserializer implements ObjectDeserializer {

    private static final HashMap<String, String> parentMap = new HashMap<>();

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object o) {
        JSONObject jsonObject = parser.parseObject();

        String name = jsonObject.getString("name");
        JSONArray objects = jsonObject.getJSONArray("objects");

        Scene scene = new Scene(name, false);
        for (int i = 0; i < objects.size(); i++) {
            scene.addObject(objects.getObject(i, GameObject.class));
        }

        parentMap.forEach((child, parent) -> {
            GameObject childObject = scene.findObject(child);
            GameObject parentObject = scene.findObject(parent);
            if (childObject != null && parentObject != null) {
                childObject.setParent(parentObject);
            }
        });
        parentMap.clear();

        return (T) scene;
    }

    public static void setParent(String child, String parent) {
        parentMap.put(child, parent);
    }

}
