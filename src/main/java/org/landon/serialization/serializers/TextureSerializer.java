package org.landon.serialization.serializers;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;
import org.landon.graphics.material.Texture;
import org.landon.project.LeapFile;

import java.io.File;
import java.lang.reflect.Type;

public class TextureSerializer implements ObjectWriter<Texture> {

    @Override
    public void write(JSONWriter jsonWriter, Object object, Object fieldName, Type fieldType, long features) {
        Texture texture = (Texture) object;
        jsonWriter.writeAs(texture.getFile(), LeapFile.class);
    }

}
