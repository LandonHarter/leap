package org.landon.serialization.deserializers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import org.landon.graphics.Mesh;

import java.lang.reflect.Type;

public class MeshDeserializer implements ObjectDeserializer {

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object o) {
        JSONObject jsonObject = parser.parseObject();

        Mesh.Vertex[] vertices = jsonObject.getObject("vertices", Mesh.Vertex[].class);
        int[] indices = jsonObject.getObject("indices", int[].class);

        return (T) new Mesh(vertices, indices);
    }

}
