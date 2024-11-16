package org.landon.serialization.deserializers;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import org.landon.project.LeapFile;
import org.landon.scene.GameObject;
import org.landon.scene.Scene;
import org.landon.skybox.DefaultSkyboxes;
import org.landon.skybox.SkyboxType;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;

public class SceneDeserializer implements ObjectReader<Scene> {

    private static final HashMap<String, String> parentMap = new HashMap<>();

    @Override
    public Scene readObject(JSONReader jsonReader, Type fieldType, Object fieldName, long features) {
        JSONObject jsonObject = jsonReader.readJSONObject();

        String name = jsonObject.getString("name");
        JSONArray objects = jsonObject.getJSONArray("objects");
        JSONObject skybox = jsonObject.getJSONObject("skybox");

        Scene scene = new Scene(name, false);
        for (int i = 0; i < objects.size(); i++) {
            JSONObject object = objects.getJSONObject(i);
            GameObject gameObject = JSON.parseObject(object.toString(), GameObject.class);
            scene.addObject(gameObject);
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
        LeapFile[] textures = JSON.parseObject(skybox.getJSONArray("textures").toString(), LeapFile[].class);
        if (textures.length == 0) textures = DefaultSkyboxes.CITY;
        scene.getSkybox().setType(skyboxType);
        scene.getSkybox().setTextures(textures);

        return scene;
    }

    public static void setParent(String child, String parent) {
        parentMap.put(child, parent);
    }

}
