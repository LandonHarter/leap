package org.landon.serialization.serializers;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;
import org.landon.serialization.types.LeapFile;
import org.landon.project.Project;

import java.lang.reflect.Type;

public class FileSerializer implements ObjectWriter<LeapFile> {

    @Override
    public void write(JSONWriter jsonWriter, Object object, Object fieldName, Type fieldType, long features) {
        LeapFile file = (LeapFile) object;

        boolean isEngineFile = !file.getAbsolutePath().contains(Project.getRootDirectory().getAbsolutePath());
        String path = isEngineFile ? file.getPath().replace(System.getProperty("user.dir"), "") : file.getAbsolutePath().replace(Project.getRootDirectory().getAbsolutePath(), "");

        jsonWriter.startObject();
        jsonWriter.writeName("path");
        jsonWriter.writeColon();
        jsonWriter.writeString(path);
        jsonWriter.writeName("isEngineFile");
        jsonWriter.writeColon();
        jsonWriter.writeBool(isEngineFile);
        jsonWriter.endObject();
    }

}
