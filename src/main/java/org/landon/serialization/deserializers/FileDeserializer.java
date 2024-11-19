package org.landon.serialization.deserializers;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import org.landon.serialization.types.LeapFile;
import org.landon.project.Project;

import java.lang.reflect.Type;

public class FileDeserializer implements ObjectReader<LeapFile> {

    @Override
    public LeapFile readObject(JSONReader jsonReader, Type fieldType, Object fieldName, long features) {
        JSONObject jsonObject = jsonReader.readJSONObject();

        String path = jsonObject.getString("path");
        boolean isEngineFile = jsonObject.getBoolean("isEngineFile");

        String newPath = isEngineFile ? System.getProperty("user.dir") + "\\\\" + path : Project.getRootDirectory().getAbsolutePath() + path;
        return new LeapFile(newPath);
    }

}
