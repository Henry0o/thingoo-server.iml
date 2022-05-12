package com.nuttu.aicloud.model.status;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nuttu.aicloud.model.device.Device;
import com.nuttu.aicloud.model.device.DeviceType;
import com.nuttu.aicloud.model.gateway.Gateway;
import com.nuttu.aicloud.mqtt.model.StatusMessage;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

@Data
@Entity
public class Status implements Serializable {
    //status uuid
    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid")
    @GeneratedValue(generator="idGenerator")
    @Getter @Setter private String uuid;
//    @Getter @Setter private String userId;
//    @Getter @Setter private String name;

    //gateway mac, means uuid
    @Getter @Setter private String gatewayMac;
    //device(include gateway) mac, means uuid
    @Getter @Setter private String mac;
    //device type
    @Enumerated(EnumType.STRING)
    @Getter @Setter private DeviceType type;
    //device name
    @Getter @Setter private String name;
    //online/offline/rebooting
    @Enumerated(EnumType.STRING)
    @Getter @Setter private StatusType status;
    //raw data
    @Getter @Setter private String rawData;
    //gateway number
    @Getter @Setter private Integer gatewayLoad;
//    @OneToOne @JoinColumn(name = "mac", referencedColumnName = "mac", insertable = false, updatable = false)
    @Transient
    @Getter @Setter private Device device;
//    @OneToOne @JoinColumn(name = "gatewayMac", referencedColumnName = "mac", insertable = false, updatable = false)
    @Transient
    @Getter @Setter private Gateway gateway;

    @Column(insertable = false, updatable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter private Date updatedAt;
    @Column(insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter private Date createdAt;

    public Status() {
    }

    public void copyFrom(String gatewayMac,StatusMessage status){
        this.gatewayMac = gatewayMac;
        this.mac = status.getMac();

        if (status.getType() == null){
            this.type = null;
        } else if (status.getType().equalsIgnoreCase("Gateway")){
            this.type = DeviceType.Gateway;
        } else if (status.getType().equalsIgnoreCase("Scanner")){
            this.type = DeviceType.Scanner;
        } else if (status.getType().equalsIgnoreCase("Camera")){
            this.type = DeviceType.Camera;
        } else if (status.getType().equalsIgnoreCase("Camera3D")){
            this.type = DeviceType.Camera3D;
        } else if (status.getType().equalsIgnoreCase("CameraAI")){
            this.type = DeviceType.CameraAI;
        } else if (status.getType().equalsIgnoreCase("IDVision")){
            this.type = DeviceType.IDVision;
        } else if (status.getType().equalsIgnoreCase("IndustrialVision")){
            this.type = DeviceType.IndustrialVision;
        } else if (status.getType().equalsIgnoreCase("Unknown")){
            this.type = DeviceType.Unknown;
        }
//        this.status = status.getStatus();

        if(status.getStatus()==null){
            this.status = null;
        }else if (status.getStatus().equalsIgnoreCase("online")){
            this.status = StatusType.online;
        }else if (status.getStatus().equalsIgnoreCase("offline")){
            this.status = StatusType.offline;
        }else if (status.getStatus().equalsIgnoreCase("rebooting")){
            this.status = StatusType.rebooting;
        }

        this.name = status.getName();
        this.rawData = status.getRawData();
        this.gatewayLoad = status.getGatewayLoad();
        this.updatedAt = status.getTimestamp();
    }

}
