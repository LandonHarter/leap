package org.landon.serialization.types;

import com.alibaba.fastjson2.annotation.JSONType;
import org.landon.serialization.deserializers.FloatDeserializer;

@JSONType(deserializer = FloatDeserializer.class)
public class LeapFloat {

    private float value;

    public LeapFloat(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float[] toArray() {
        return new float[] { value };
    }

}
