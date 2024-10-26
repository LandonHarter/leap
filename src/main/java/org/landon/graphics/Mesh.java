package org.landon.graphics;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Mesh {

    private Vertex[] vertices;
    private int[] indices;

    private int vao, pbo, ibo, tbo, nbo;
    private boolean created = false;

    public Mesh(Vertex[] vertices, int[] indices) {
        this.vertices = vertices;
        this.indices = indices;

        create();
    }

    public Mesh(Vertex[] vertices, int[] indices, boolean createMesh) {
        this.vertices = vertices;
        this.indices = indices;

        if (createMesh) create();
    }

    public void create() {
        if (created) return;

        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);

        FloatBuffer positionBuffer = MemoryUtil.memAllocFloat(vertices.length * 3);
        float[] positionData = new float[vertices.length * 3];
        for (int i = 0; i < vertices.length; i++) {
            positionData[i * 3] = vertices[i].getPosition().x;
            positionData[i * 3 + 1] = vertices[i].getPosition().y;
            positionData[i * 3 + 2] = vertices[i].getPosition().z;
        }
        positionBuffer.put(positionData).flip();
        pbo = storeData(positionBuffer, 0, 3);

        FloatBuffer textureBuffer = MemoryUtil.memAllocFloat(vertices.length * 2);
        float[] textureData = new float[vertices.length * 2];
        for (int i = 0; i < vertices.length; i++) {
            textureData[i * 2] = vertices[i].getTextureCoordinates().x;
            textureData[i * 2 + 1] = vertices[i].getTextureCoordinates().y;
        }
        textureBuffer.put(textureData).flip();
        tbo = storeData(textureBuffer, 1, 2);

        IntBuffer indicesBuffer = MemoryUtil.memAllocInt(indices.length);
        indicesBuffer.put(indices).flip();

        ibo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

        FloatBuffer normalBuffer = MemoryUtil.memAllocFloat(vertices.length * 3);
        float[] normalData = new float[vertices.length * 3];
        for (int i = 0; i < vertices.length; i++) {
            normalData[i * 3] = vertices[i].getNormal().x;
            normalData[i * 3 + 1] = vertices[i].getNormal().y;
            normalData[i * 3 + 2] = vertices[i].getNormal().z;
        }
        normalBuffer.put(normalData).flip();
        nbo = storeData(normalBuffer, 2, 3);

        created = true;
    }

    public void calculateNormals() {
        for (int i = 0; i < indices.length; i += 3) {
            Vertex a = vertices[indices[i]];
            Vertex b = vertices[indices[i + 1]];
            Vertex c = vertices[indices[i + 2]];

            Vector3f edge1 = a.getPosition().sub(b.getPosition());
            Vector3f edge2 = b.getPosition().sub(c.getPosition());
            Vector3f normal = edge1.cross(edge2).normalize();

            a.setNormal(normal);
            b.setNormal(normal);
            c.setNormal(normal);
        }
    }

    public void destroy() {
        if (!created) return;

        GL15.glDeleteBuffers(pbo);
        GL15.glDeleteBuffers(ibo);
        GL15.glDeleteBuffers(tbo);
        GL15.glDeleteBuffers(nbo);
        GL30.glDeleteVertexArrays(vao);

        vao = 0;
        pbo = 0;
        ibo = 0;
        tbo = 0;
        nbo = 0;
    }

    private int storeData(FloatBuffer buffer, int index, int size) {
        int bufferID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, bufferID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(index, size, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        return bufferID;
    }

    public Vertex[] getVertices() {
        return vertices;
    }

    public int[] getIndices() {
        return indices;
    }

    public int getVao() {
        return vao;
    }

    public int getPbo() {
        return pbo;
    }

    public int getIbo() {
        return ibo;
    }

    public int getTbo() {
        return tbo;
    }

    public int getNbo() {
        return nbo;
    }

    public static class Vertex {

        private Vector3f position;
        private Vector2f textureCoordinates;
        private Vector3f normal;

        public Vertex(Vector3f position, Vector2f textureCoordinates) {
            this.position = position;
            this.textureCoordinates = textureCoordinates;
            this.normal = new Vector3f(0);
        }

        public Vertex(Vector3f position, Vector2f textureCoordinates, Vector3f normal) {
            this.position = position;
            this.textureCoordinates = textureCoordinates;
            this.normal = normal;
        }

        public Vector3f getPosition() {
            return position;
        }

        public void setPosition(Vector3f newPosition) {
            position = newPosition;
        }

        public Vector2f getTextureCoordinates() {
            return textureCoordinates;
        }

        public void setTextureCoordinates(Vector2f newTextureCoordinates) {
            textureCoordinates = newTextureCoordinates;
        }

        public Vector3f getNormal() {
            return normal;
        }

        public void setNormal(Vector3f newNormal) {
            normal = newNormal;
        }

    }
}
