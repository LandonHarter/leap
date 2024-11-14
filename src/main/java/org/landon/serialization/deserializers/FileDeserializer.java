package org.landon.serialization.deserializers;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import org.landon.project.Project;

import java.io.File;
import java.lang.reflect.Type;

public class FileDeserializer implements ObjectDeserializer {

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        JSONObject jsonObject = parser.parseObject();

        String path = jsonObject.getString("path");
        boolean isEngineFile = jsonObject.getBoolean("isEngineFile");

        String newPath = isEngineFile ? System.getProperty("user.dir") + "\\\\" + path : Project.getRootDirectory().getAbsolutePath() + path;
        return (T) new File(newPath);
    }

}
