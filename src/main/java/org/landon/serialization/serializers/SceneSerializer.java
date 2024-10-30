package org.landon.serialization.serializers;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import org.landon.scene.Scene;

import java.io.IOException;
import java.lang.reflect.Type;

public final class SceneSerializer implements ObjectSerializer {

    @Override
    public void write(JSONSerializer serializer, Object o, Object o1, Type type, int i) throws IOException {
        Scene scene = (Scene) o;

        JSONObject object = new JSONObject();
        object.put("name", scene.getName());
        object.put("objects", scene.getObjects().toArray());

        serializer.write(object);
    }

}
