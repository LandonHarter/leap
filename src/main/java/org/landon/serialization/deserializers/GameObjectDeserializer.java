package org.landon.serialization.deserializers;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import org.landon.components.Component;
import org.landon.math.Transform;
import org.landon.scene.GameObject;

import java.lang.reflect.Type;

public class GameObjectDeserializer implements ObjectReader<GameObject> {

    @Override
    public GameObject readObject(JSONReader jsonReader, Type fieldType, Object fieldName, long features) {
        JSONObject jsonObject = jsonReader.readJSONObject();

        String uuid = jsonObject.getString("uuid");
        String name = jsonObject.getString("name");
        Transform transform = jsonObject.getObject("transform", Transform.class);

        JSONArray componentsArray = jsonObject.getJSONArray("components");
        Component[] components = new Component[componentsArray.size()];
        for (int i = 0; i < componentsArray.size(); i++) {
            JSONObject componentObject = componentsArray.getJSONObject(i);
            Component component = JSON.parseObject(componentObject.toString(), Component.class);
            components[i] = component;
        }

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

        return gameObject;
    }

}
