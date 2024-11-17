package org.landon.graphics.mesh;

import org.joml.*;
import org.landon.components.graphics.MeshFilter;
import org.landon.components.rendering.MeshRenderer;
import org.landon.editor.windows.logger.Logger;
import org.landon.graphics.material.Material;
import org.landon.graphics.material.Texture;
import org.landon.scene.GameObject;
import org.landon.util.LoadingUtil;
import org.lwjgl.assimp.*;

import java.io.File;
import java.lang.Math;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class ModelLoader {

    public static GameObject loadModel(String path) {
        return loadModel(path, false);
    }

    public static GameObject loadModel(String path, boolean loadTextures) {
        LoadingUtil.openLoadingScreen("Loading model...");
        int fast = Assimp.aiProcess_GenNormals | Assimp.aiProcess_JoinIdenticalVertices | Assimp.aiProcess_Triangulate | Assimp.aiProcess_SortByPType;
        AIScene scene = Assimp.aiImportFile(path, fast);

        if (scene == null) {
            Logger.error("Failed to load model: " + path);
            LoadingUtil.closeLoadingBar();
            return new GameObject();
        }

        File file = new File(path);
        GameObject root = loadGameObject(file, scene, scene.mRootNode(), loadTextures);
        root.setName(new File(path).getName().split("[.]")[0]);

        Assimp.aiFreeScene(scene);

        LoadingUtil.closeLoadingBar();

        return root;
    }

    private static GameObject loadGameObject(File file, AIScene scene, AINode node, boolean loadTextures) {
        GameObject gameObject = new GameObject(node.mName().dataString());

        Matrix4f transform = fromAssimpMatrix(node.mTransformation());
        Vector3f position = transform.getTranslation(new Vector3f());
        Quaternionf rotation = transform.getUnnormalizedRotation(new Quaternionf());
        Vector3f scale = transform.getScale(new Vector3f());

        gameObject.getTransform().setLocalPosition(position);
        gameObject.getTransform().setLocalRotation(rotation.getEulerAnglesXYZ(new Vector3f()).mul(180.0f / (float) Math.PI));
        gameObject.getTransform().setLocalScale(scale);

        loadComponents(file, scene, node, gameObject, loadTextures);

        for (int i = 0; i < node.mNumChildren(); i++) {
            GameObject child = loadGameObject(file, scene, AINode.create(node.mChildren().get(i)), loadTextures);
            gameObject.addChild(child);
        }

        return gameObject;
    }

    private static void loadComponents(File file, AIScene scene, AINode node, GameObject gameObject, boolean loadTextures) {
        for (int i = 0; i < node.mNumMeshes(); i++) {
            AIMesh mesh = AIMesh.create(scene.mMeshes().get(node.mMeshes().get(i)));
            GameObject child = loadMesh(file, scene, mesh, loadTextures);
            gameObject.addChild(child);
        }
    }

    private static GameObject loadMesh(File file, AIScene scene, AIMesh mesh, boolean loadTextures) {
        GameObject obj = new GameObject(mesh.mName().dataString());
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

        Mesh m = new Mesh(vertices, indices, true);
        Material mat = loadMaterial(file, AIMaterial.create(scene.mMaterials().get(mesh.mMaterialIndex())), loadTextures);

        obj.addComponent(new MeshFilter(m, mat));
        obj.addComponent(new MeshRenderer());

        return obj;
    }

    private static Material loadMaterial(File file, AIMaterial material, boolean loadTextures) {
        Material mat = new Material();

        for (int i = 0; i < material.mNumProperties(); i++) {
            AIMaterialProperty property = AIMaterialProperty.create(material.mProperties().get(i));
            String key = property.mKey().dataString();
            ByteBuffer data = property.mData();

            switch (key) {
                case Assimp.AI_MATKEY_BASE_COLOR -> {
                    mat.setColor(new Vector4f(data.getFloat(), data.getFloat(), data.getFloat(), data.getFloat()));
                    break;
                }
            }
        }

        if (loadTextures) {
            AIString path = AIString.create();
            int result;

            result = Assimp.aiGetMaterialTexture(material, Assimp.aiTextureType_DIFFUSE, 0, path, (IntBuffer) null, null, null, null, null, null);
            if (result == Assimp.aiReturn_SUCCESS) {
                mat.setTexture(new Texture(new File(file.getParent() + "/" + path.dataString())));
            }

            result = Assimp.aiGetMaterialTexture(material, Assimp.aiTextureType_NORMALS, 0, path, (IntBuffer) null, null, null, null, null, null);
            if (result == Assimp.aiReturn_SUCCESS) {
                mat.setNormalMap(new Texture(new File(file.getParent() + "/" + path.dataString())));
            }
        }

        return mat;
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
