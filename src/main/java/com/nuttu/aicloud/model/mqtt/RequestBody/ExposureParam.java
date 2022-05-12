package com.nuttu.aicloud.model.mqtt.RequestBody;

import lombok.Getter;
import lombok.Setter;

public class ExposureParam {
    @Getter @Setter
    private int exp;
    @Getter @Setter
    private int dgain;
    @Getter @Setter
    private int again ;
}
