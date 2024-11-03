package org.landon.serialization.deserializers;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import org.landon.graphics.Texture;

import java.lang.reflect.Type;

public class TextureDeserializer implements ObjectDeserializer {

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object o) {
        String texturePath = parser.parseObject(String.class);
        return (T) new Texture(texturePath);
    }

}
