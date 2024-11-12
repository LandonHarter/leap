package org.landon.serialization;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.landon.components.Component;
import org.landon.graphics.Mesh;
import org.landon.graphics.Texture;
import org.landon.scene.GameObject;
import org.landon.scene.Scene;
import org.landon.serialization.deserializers.*;
import org.landon.serialization.serializers.FileSerializer;
import org.landon.serialization.serializers.GameObjectSerializer;
import org.landon.serialization.serializers.SceneSerializer;
import org.landon.serialization.serializers.TextureSerializer;

import java.io.File;
import java.lang.reflect.Type;

public final class Serializer {

    private static final SerializeConfig serializeConfig = new SerializeConfig(true);

    public static void init() {
        serializeConfig.put(Scene.class, new SceneSerializer());
        serializeConfig.put(GameObject.class, new GameObjectSerializer());
        serializeConfig.put(Texture.class, new TextureSerializer());
        serializeConfig.put(File.class, new FileSerializer());
    }

    public static String toJson(Object object) {
        return JSON.toJSONStringZ(object, serializeConfig, SerializerFeature.PrettyFormat, SerializerFeature.SkipTransientField, SerializerFeature.QuoteFieldNames);
    }

    public static <T> T fromJson(String json, Type type) {
        DefaultJSONParser parser = new DefaultJSONParser(json);

        parser.getConfig().putDeserializer(Scene.class, new SceneDeserializer());
        parser.getConfig().putDeserializer(GameObject.class, new GameObjectDeserializer());
        parser.getConfig().putDeserializer(Component.class, new ComponentDeserializer());
        parser.getConfig().putDeserializer(Texture.class, new TextureDeserializer());
        parser.getConfig().putDeserializer(Mesh.class, new MeshDeserializer());
        parser.getConfig().putDeserializer(File.class, new FileDeserializer());

        return parser.parseObject(type);
    }

}
