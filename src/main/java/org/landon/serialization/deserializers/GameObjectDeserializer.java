package org.landon.serialization.deserializers;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import org.landon.components.Component;
import org.landon.math.Transform;
import org.landon.scene.GameObject;

import java.lang.reflect.Type;

public class GameObjectDeserializer implements ObjectDeserializer {

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object o) {
        JSONObject jsonObject = parser.parseObject();

        String uuid = jsonObject.getString("uuid");
        String name = jsonObject.getString("name");
        Transform transform = jsonObject.getObject("transform", Transform.class);
        Component[] components = jsonObject.getObject("components", Component[].class);
        String parent = jsonObject.getString("parent");

        GameObject gameObject = new GameObject(name);
        gameObject.setUuid(uuid);
        gameObject.setTransform(transform);
        for (Component component : components) {
            gameObject.addComponent(component);
            component.createGizmo();
        }

        if (parent != null) {
            SceneDeserializer.setParent(uuid, parent);
        }

        return (T) gameObject;
    }

}
