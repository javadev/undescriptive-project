package com.github.javadev.undescriptive.protocol.request;

import java.util.LinkedHashMap;
import java.util.Map;

public class SolutionRequest implements HasParams {
    private final Integer scale;
    private final Integer claw;
    private final Integer wing;
    private final Integer fire;

    public SolutionRequest(
        final Integer scale,
        final Integer claw,
        final Integer wing,
        final Integer fire
            ) {
        this.scale = scale;
        this.claw = claw;
        this.wing = wing;
        this.fire = fire;
    }

    public Map<String, Object> getParams() {
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        Map<String, Object> dragonParams = new LinkedHashMap<String, Object>();
        dragonParams.put("scaleThickness", scale);
        dragonParams.put("clawSharpness", claw);
        dragonParams.put("wingStrength", wing);
        dragonParams.put("fireBreath", fire);
        params.put("dragon", dragonParams);
        return params;
    }

    @Override
    public String toString() {
        return "SolutionRequest[" + scale + ", " + claw + ", " + wing + ", " + fire + "]";
    }

    public static Builder builder() {
        return new Builder();
    }

    public Integer getScale() {
        return scale;
    }

    public Integer getClaw() {
        return claw;
    }

    public Integer getWing() {
        return wing;
    }

    public Integer getFire() {
        return fire;
    }

    public static class Builder {
        private Integer scale;
        private Integer claw;
        private Integer wing;
        private Integer fire;

        private Builder() {
        }

        public Builder scale(final Integer scale) {
            this.scale = scale;
            return this;
        }

        public Builder claw(final Integer claw) {
            this.claw = claw;
            return this;
        }

        public Builder wing(final Integer wing) {
            this.wing = wing;
            return this;
        }

        public Builder fire(final Integer fire) {
            this.fire = fire;
            return this;
        }

        public SolutionRequest build() {
            return new SolutionRequest(scale, claw, wing, fire);
        }
    }
}
