package org.landon.graphics;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.landon.components.graphics.MeshFilter;
import org.landon.components.rendering.MeshRenderer;
import org.landon.editor.windows.logger.Logger;
import org.landon.scene.GameObject;
import org.landon.util.LoadingUtil;
import org.lwjgl.assimp.*;

import java.io.File;

public class ModelLoader {

    public static GameObject loadModel(String path) {
        LoadingUtil.openLoadingScreen("Loading model...");
        int fast = Assimp.aiProcess_GenNormals | Assimp.aiProcess_JoinIdenticalVertices | Assimp.aiProcess_Triangulate | Assimp.aiProcess_SortByPType;
        AIScene scene = Assimp.aiImportFile(path, fast);

        if (scene == null) {
            Logger.error("Failed to load model: " + path);
            LoadingUtil.closeLoadingBar();
            return new GameObject();
        }

        GameObject root = loadGameObject(scene, scene.mRootNode());
        root.setName(new File(path).getName().split("[.]")[0]);

        Assimp.aiFreeScene(scene);

        LoadingUtil.closeLoadingBar();

        return root;
    }

    private static GameObject loadGameObject(AIScene scene, AINode node) {
        GameObject gameObject = new GameObject(node.mName().dataString());

        Matrix4f transform = fromAssimpMatrix(node.mTransformation());
        Vector3f position = transform.getTranslation(new Vector3f());
        Quaternionf rotation = transform.getUnnormalizedRotation(new Quaternionf());
        Vector3f scale = transform.getScale(new Vector3f());

        gameObject.getTransform().setLocalPosition(position);
        gameObject.getTransform().setLocalRotation(rotation.getEulerAnglesXYZ(new Vector3f()).mul(180.0f / (float) Math.PI));
        gameObject.getTransform().setLocalScale(scale);

        loadComponents(scene, node, gameObject);

        for (int i = 0; i < node.mNumChildren(); i++) {
            GameObject child = loadGameObject(scene, AINode.create(node.mChildren().get(i)));
            gameObject.addChild(child);
        }

        return gameObject;
    }

    private static void loadComponents(AIScene scene, AINode node, GameObject gameObject) {
        for (int i = 0; i < node.mNumMeshes(); i++) {
            AIMesh mesh = AIMesh.create(scene.mMeshes().get(node.mMeshes().get(i)));
            Mesh m = loadMesh(scene, mesh);

            GameObject child = new GameObject(mesh.mName().dataString());
            child.addComponent(new MeshFilter(m));
            child.addComponent(new MeshRenderer());
            gameObject.addChild(child);
        }
    }

    private static Mesh loadMesh(AIScene scene, AIMesh mesh) {
        Mesh.Vertex[] vertices = new Mesh.Vertex[mesh.mNumVertices()];
        for (int i = 0; i < mesh.mNumVertices(); i++) {
            AIVector3D vertex = mesh.mVertices().get(i);
            AIVector3D normal = mesh.mNormals().get(i);
            AIVector3D texCoord = mesh.mTextureCoords(0).get(i);

            Vector3f position = new Vector3f(vertex.x(), vertex.y(), vertex.z());
            Vector3f normalVec = new Vector3f(normal.x(), normal.y(), normal.z());
            Vector2f texCoordVec = new Vector2f(texCoord.x(), texCoord.y());

            vertices[i] = new Mesh.Vertex(position, texCoordVec, normalVec);
        }

        int[] indices = new int[mesh.mNumFaces() * 3];
        for (int i = 0; i < mesh.mNumFaces(); i++) {
            AIFace face = mesh.mFaces().get(i);
            indices[i * 3] = face.mIndices().get(0);
            indices[i * 3 + 1] = face.mIndices().get(1);
            indices[i * 3 + 2] = face.mIndices().get(2);
        }

        return new Mesh(vertices, indices, true);
    }

    private static Matrix4f fromAssimpMatrix(AIMatrix4x4 matrix) {
        return new Matrix4f(
                matrix.a1(), matrix.a2(), matrix.a3(), matrix.a4(),
                matrix.b1(), matrix.b2(), matrix.b3(), matrix.b4(),
                matrix.c1(), matrix.c2(), matrix.c3(), matrix.c4(),
                matrix.d1(), matrix.d2(), matrix.d3(), matrix.d4()
        );
    }

}
