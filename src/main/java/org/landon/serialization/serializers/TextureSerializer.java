package org.landon.serialization.serializers;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import org.landon.graphics.Texture;

import java.io.IOException;
import java.lang.reflect.Type;

public class TextureSerializer implements ObjectSerializer {

    @Override
    public void write(JSONSerializer serializer, Object o, Object o1, Type type, int i) throws IOException {
        Texture texture = (Texture) o;
        serializer.write(texture.getFile());
    }

}
