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

        String name = jsonObject.getString("name");
        Transform transform = jsonObject.getObject("transform", Transform.class);
        Component[] components = jsonObject.getObject("components", Component[].class);

        GameObject gameObject = new GameObject(name);
        gameObject.setTransform(transform);
        for (Component component : components) {
            gameObject.addComponent(component);
        }

        return (T) gameObject;
    }

}
