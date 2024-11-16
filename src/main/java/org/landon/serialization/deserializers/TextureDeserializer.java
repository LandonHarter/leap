package org.landon.serialization.deserializers;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import org.landon.graphics.material.Texture;
import org.landon.project.LeapFile;

import java.io.File;
import java.lang.reflect.Type;

public class TextureDeserializer implements ObjectReader<Texture> {

    @Override
    public Texture readObject(JSONReader jsonReader, Type fieldType, Object fieldName, long features) {
        LeapFile file = jsonReader.read(LeapFile.class);
        if (!file.exists()) return new Texture("resources/textures/default.png");
        return new Texture(file.getAbsolutePath());
    }

}
