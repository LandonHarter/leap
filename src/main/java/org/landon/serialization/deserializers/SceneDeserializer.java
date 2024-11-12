package org.landon.serialization.deserializers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import org.landon.project.Project;
import org.landon.scene.GameObject;
import org.landon.scene.Scene;
import org.landon.skybox.DefaultSkyboxes;
import org.landon.skybox.SkyboxType;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;

public class SceneDeserializer implements ObjectDeserializer {

    private static final HashMap<String, String> parentMap = new HashMap<>();

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object o) {
        JSONObject jsonObject = parser.parseObject();

        String name = jsonObject.getString("name");
        JSONArray objects = jsonObject.getJSONArray("objects");
        JSONObject skybox = jsonObject.getJSONObject("skybox");

        Scene scene = new Scene(name, false);
        for (int i = 0; i < objects.size(); i++) {
            scene.addObject(objects.getObject(i, GameObject.class));
        }

        parentMap.forEach((child, parent) -> {
            GameObject childObject = scene.findObject(child);
            GameObject parentObject = scene.findObject(parent);
            if (childObject != null && parentObject != null) {
                parentObject.addChild(childObject);
            }
        });
        parentMap.clear();

        SkyboxType skyboxType = SkyboxType.valueOf(skybox.getString("type"));
        File[] textures = skybox.getObject("textures", File[].class);
        if (textures.length == 0) textures = DefaultSkyboxes.CITY;
        scene.getSkybox().setType(skyboxType);
        scene.getSkybox().setTextures(textures);

        return (T) scene;
    }

    public static void setParent(String child, String parent) {
        parentMap.put(child, parent);
    }

}
