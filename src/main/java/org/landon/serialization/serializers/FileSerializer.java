package org.landon.serialization.serializers;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import org.landon.project.Project;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;

public class FileSerializer implements ObjectSerializer {

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        File file = (File) object;

        boolean isEngineFile = !file.getAbsolutePath().contains(Project.getRootDirectory().getAbsolutePath());
        String path = isEngineFile ? file.getPath().replace(System.getProperty("user.dir"), "") : file.getAbsolutePath().replace(Project.getRootDirectory().getAbsolutePath(), "");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("path", path);
        jsonObject.put("isEngineFile", isEngineFile);

        serializer.write(jsonObject);
    }

}
