package org.landon.serialization;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.landon.graphics.Texture;
import org.landon.scene.GameObject;
import org.landon.scene.Scene;
import org.landon.serialization.serializers.GameObjectSerializer;
import org.landon.serialization.serializers.SceneSerializer;
import org.landon.serialization.serializers.TextureSerializer;

public final class Serializer {

    private static final SerializeConfig config = new SerializeConfig(true);

    public static void init() {
        config.put(Scene.class, new SceneSerializer());
        config.put(GameObject.class, new GameObjectSerializer());
        config.put(Texture.class, new TextureSerializer());
    }

    public static String toJson(Object object) {
        return JSON.toJSONStringZ(object, config, SerializerFeature.PrettyFormat, SerializerFeature.SkipTransientField, SerializerFeature.QuoteFieldNames);
    }

}
