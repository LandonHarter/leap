package org.landon.serialization.deserializers;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import org.landon.graphics.Texture;
import org.landon.project.Project;

import java.io.File;
import java.lang.reflect.Type;

public class TextureDeserializer implements ObjectDeserializer {

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object o) {
        File file = parser.parseObject(File.class);
        return (T) new Texture(file.getAbsolutePath());
    }

}
