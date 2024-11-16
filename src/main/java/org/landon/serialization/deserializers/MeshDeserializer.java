package org.landon.serialization.deserializers;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import org.landon.graphics.mesh.Mesh;

import java.lang.reflect.Type;

public class MeshDeserializer implements ObjectReader<Mesh> {

    @Override
    public Mesh readObject(JSONReader jsonReader, Type fieldType, Object fieldName, long features) {
        JSONObject jsonObject = jsonReader.readJSONObject();

        Mesh.Vertex[] vertices = jsonObject.getObject("vertices", Mesh.Vertex[].class);
        int[] indices = jsonObject.getObject("indices", int[].class);

        return new Mesh(vertices, indices);
    }

}