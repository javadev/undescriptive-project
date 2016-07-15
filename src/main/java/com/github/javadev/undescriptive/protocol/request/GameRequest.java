package com.github.javadev.undescriptive.protocol.request;

import java.util.LinkedHashMap;
import java.util.Map;

public class GameRequest implements HasParams {
    public Integer scale;
    public Integer claw;
    public Integer wing;
    public Integer fire;

    public Map<String, Object> getParams() {
        return new LinkedHashMap<String, Object>() { {
            put("dragon", new LinkedHashMap<String, Object>() { {
                put("scaleThickness", scale);
                put("clawSharpness", claw);
                put("wingStrength", wing);
                put("fireBreath", fire);
            } });
        } };
    }
}
