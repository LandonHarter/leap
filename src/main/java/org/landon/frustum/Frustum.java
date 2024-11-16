package org.landon.frustum;

import org.joml.FrustumIntersection;
import org.joml.Matrix4f;
import org.landon.scene.SceneManager;

public class Frustum {

    private static FrustumIntersection frustum = new FrustumIntersection();

    public static void update() {
        Matrix4f viewProjection = new Matrix4f(SceneManager.getProjectionMatrix()).mul(SceneManager.getViewMatrix());
        frustum.set(viewProjection);
    }

    public static boolean inFrustum(AABB aabb) {
        if (aabb == null) return true;
        return frustum.testAab(aabb.getMin(), aabb.getMax());
    }

}
