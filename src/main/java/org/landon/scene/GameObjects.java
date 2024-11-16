package org.landon.scene;

import org.landon.graphics.mesh.ModelLoader;

public class GameObjects {

    public static GameObject createCube() {
        GameObject obj = ModelLoader.loadModel("resources/models/cube.fbx");
        GameObject child = obj.getChildren().get(0);
        GameObject child2 = child.getChildren().get(0);
        obj.removeChild(child);
        child.removeChild(child2);
        obj.destroy();
        child.destroy();
        child2.getTransform().reset();

        return child2;
    }

    public static GameObject createSphere() {
        GameObject obj = ModelLoader.loadModel("resources/models/sphere.fbx");
        GameObject child = obj.getChildren().get(0);
        GameObject child2 = child.getChildren().get(0);
        obj.removeChild(child);
        child.removeChild(child2);
        obj.destroy();
        child.destroy();
        child2.getTransform().reset();

        return child2;
    }

}
