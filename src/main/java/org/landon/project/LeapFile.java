package org.landon.project;

import com.alibaba.fastjson2.annotation.JSONType;
import org.landon.serialization.deserializers.FileDeserializer;
import org.landon.serialization.serializers.FileSerializer;

import java.io.File;
import java.net.URI;

@JSONType(serializer = FileSerializer.class, deserializer = FileDeserializer.class)
public class LeapFile extends File {

    public LeapFile(String pathname) {
        super(pathname);
    }

    public LeapFile(String parent, String child) {
        super(parent, child);
    }

    public LeapFile(File parent, String child) {
        super(parent, child);
    }

    public LeapFile(URI uri) {
        super(uri);
    }

    public LeapFile(File file) {
        super(file.getAbsolutePath());
    }

}
