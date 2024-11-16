package org.landon.serialization.serializers;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;
import org.landon.project.LeapFile;
import org.landon.scene.GameObject;
import org.landon.scene.Scene;
import org.landon.serialization.Serializer;

import java.io.File;
import java.lang.reflect.Type;

public final class SceneSerializer implements ObjectWriter<Scene> {

    @Override
    public void write(JSONWriter jsonWriter, Object object, Object fieldName, Type fieldType, long features) {
        Scene scene = (Scene) object;

        jsonWriter.startObject();

        jsonWriter.writeName("name");
        jsonWriter.writeColon();
        jsonWriter.writeString(scene.getName());

        jsonWriter.writeName("objects");
        jsonWriter.writeColon();

        jsonWriter.writeAs(scene.getObjects().toArray(new GameObject[0]), GameObject[].class);

        jsonWriter.writeName("skybox");
        jsonWriter.writeColon();
        jsonWriter.startObject();
        jsonWriter.writeName("type");
        jsonWriter.writeColon();
        jsonWriter.writeString(scene.getSkybox().getType().name());
        jsonWriter.writeName("textures");
        jsonWriter.writeColon();

        jsonWriter.writeAs(scene.getSkybox().getTextures(), LeapFile[].class);

        jsonWriter.endObject();

        jsonWriter.endObject();
    }

}
