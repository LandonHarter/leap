package org.landon.serialization;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.reader.ObjectReaderProvider;
import com.alibaba.fastjson2.writer.ObjectWriterProvider;
import org.landon.components.Component;
import org.landon.graphics.material.Texture;
import org.landon.graphics.mesh.Mesh;
import org.landon.serialization.types.LeapEnum;
import org.landon.serialization.types.LeapFile;
import org.landon.scene.GameObject;
import org.landon.scene.Scene;
import org.landon.serialization.deserializers.*;
import org.landon.serialization.serializers.*;
import org.landon.serialization.types.LeapFloat;

import java.lang.reflect.Type;

public final class Serializer {

    private static JSONWriter.Context writeContext;
    private static JSONReader.Context readContext;

    public static void init() {
        ObjectWriterProvider writeProvider = JSONFactory.getDefaultObjectWriterProvider();
        writeProvider.register(LeapFile.class, new FileSerializer());
        writeProvider.register(Texture.class, new TextureSerializer());
        writeProvider.register(GameObject.class, new GameObjectSerializer());
        writeProvider.register(Scene.class, new SceneSerializer());
        writeProvider.register(LeapEnum.class, new EnumSerializer());
        writeContext = JSONFactory.createWriteContext(writeProvider, JSONWriter.Feature.LargeObject, JSONWriter.Feature.FieldBased, JSONWriter.Feature.PrettyFormat);
        writeContext.setPropertyPreFilter(new TransientFilter());

        ObjectReaderProvider readProvider = JSONFactory.getDefaultObjectReaderProvider();
        readProvider.register(LeapFile.class, new FileDeserializer());
        readProvider.register(Texture.class, new TextureDeserializer());
        readProvider.register(GameObject.class, new GameObjectDeserializer());
        readProvider.register(Scene.class, new SceneDeserializer());
        readProvider.register(Component.class, new ComponentDeserializer());
        readProvider.register(Mesh.class, new MeshDeserializer());
        readProvider.register(LeapFloat.class, new FloatDeserializer());
        readProvider.register(LeapEnum.class, new EnumDeserializer());
        readContext = JSONFactory.createReadContext(readProvider, JSONReader.Feature.FieldBased);
    }

    public static String toJson(Object object) {
        return JSON.toJSONString(object, writeContext);
    }

    public static <T> T fromJson(String json, Type type) {
        return JSON.parseObject(json, type, readContext);
    }

}
