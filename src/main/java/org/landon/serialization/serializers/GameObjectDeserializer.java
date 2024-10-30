package org.landon.serialization.serializers;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.ObjectDeserializer;

import java.lang.reflect.Type;

public class GameObjectDeserializer implements ObjectDeserializer {

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object o) {
        Object object = parser.parseObject();
        System.out.println(object);
        return (T) object;
    }

}
