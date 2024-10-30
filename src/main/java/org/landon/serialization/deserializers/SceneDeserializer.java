package org.landon.serialization.deserializers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import org.landon.scene.GameObject;
import org.landon.scene.Scene;

import java.lang.reflect.Type;

public class SceneDeserializer implements ObjectDeserializer {

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object o) {
        JSONObject jsonObject = parser.parseObject();

        String name = jsonObject.getString("name");
        JSONArray objects = jsonObject.getJSONArray("objects");

        Scene scene = new Scene(name, false);
        for (int i = 0; i < objects.size(); i++) {
            scene.addObject(objects.getObject(i, GameObject.class));
        }

        return (T) scene;
    }

}
