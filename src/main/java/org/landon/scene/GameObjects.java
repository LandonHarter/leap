package org.landon.scene;

import org.landon.graphics.ModelLoader;

public class GameObjects {

    public static GameObject createCube() {
        GameObject obj = ModelLoader.loadModel("resources/models/cube.fbx");
        GameObject child = obj.getChildren().get(0);
        obj.removeChild(child);
        obj.destroy();
        child.getTransform().reset();

        return child;
    }

    public static GameObject createSphere() {
        GameObject obj = ModelLoader.loadModel("resources/models/sphere.fbx");
        GameObject child = obj.getChildren().get(0);
        obj.removeChild(child);
        obj.destroy();
        child.getTransform().reset();

        return child;
    }

}
