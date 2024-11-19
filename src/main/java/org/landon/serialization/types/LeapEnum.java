package org.landon.serialization.types;

import com.alibaba.fastjson2.annotation.JSONType;
import org.landon.serialization.deserializers.EnumDeserializer;
import org.landon.serialization.serializers.EnumSerializer;

@JSONType(serializer = EnumSerializer.class, deserializer = EnumDeserializer.class)
public class LeapEnum<T extends Enum> {

    private T value;

    public LeapEnum(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public void setValue(int value) {
        this.value = (T) this.value.getClass().getEnumConstants()[value];
    }

    @Override
    public String toString() {
        return value.toString();
    }

}
