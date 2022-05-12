package com.nuttu.aicloud.mqtt.model;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class StatusMessage {

    @Getter @Setter private String gatewayMac;
    @Getter @Setter private String mac;
    @Getter @Setter private String type;
    @Getter @Setter private String name;
    @Getter @Setter private String status;
    @Getter @Setter private String rawData;
    @Getter @Setter private Integer gatewayLoad;

    @Getter @Setter private Date timestamp;

    @Override
    public String toString() {
        return "StatusMessage{" +
                "gatewayMac='" + gatewayMac + '\'' +
                ", mac='" + mac + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", rawData='" + rawData + '\'' +
                ", gatewayLoad=" + gatewayLoad +
                ", timestamp=" + timestamp +
                '}';
    }
}
