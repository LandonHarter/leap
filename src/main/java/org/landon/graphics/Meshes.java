package org.landon.graphics;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.util.par.ParShapes;
import org.lwjgl.util.par.ParShapesMesh;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public final class Meshes {

    public static Mesh createCube() {
        return parShapeToMesh(ParShapes.par_shapes_create_cube());
    }

    public static Mesh createSphere() {
        return parShapeToMesh(ParShapes.par_shapes_create_subdivided_sphere(4));
    }

    public static Mesh parShapeToMesh(ParShapesMesh parShapesMesh) {
        ParShapes.par_shapes_rotate(parShapesMesh, (float) Math.toRadians(-90), new float[] { 1, 0, 0 });

        Mesh.Vertex[] vertices = new Mesh.Vertex[parShapesMesh.npoints()];

        FloatBuffer positions = parShapesMesh.points(parShapesMesh.npoints() * 3);
        FloatBuffer textureCoordinates = parShapesMesh.tcoords(parShapesMesh.npoints() * 2);
        FloatBuffer normals = parShapesMesh.normals(parShapesMesh.npoints() * 3);

        for (int i = 0; i < vertices.length; i++) {
            Vector3f position = new Vector3f(positions.get(i * 3), positions.get(i * 3 + 1), positions.get(i * 3 + 2));

            Vector2f textureCoordinate = new Vector2f();
            if (textureCoordinates != null) {
                textureCoordinate = new Vector2f(textureCoordinates.get(i * 2), textureCoordinates.get(i * 2 + 1));
            }

            Vector3f normal = new Vector3f();
            if (normals != null) {
                normal = new Vector3f(normals.get(i * 3), normals.get(i * 3 + 1), normals.get(i * 3 + 2));
            }

            vertices[i] = new Mesh.Vertex(position, textureCoordinate, normal);
        }

        int[] indices = new int[parShapesMesh.ntriangles() * 3];
        IntBuffer triangles = parShapesMesh.triangles(parShapesMesh.ntriangles() * 3);
        triangles.get(indices);

        ParShapes.par_shapes_free_mesh(parShapesMesh);
        Mesh mesh = new Mesh(vertices, indices, false);
        if (normals == null) mesh.calculateNormals();
        mesh.create();
        return mesh;
    }

}
