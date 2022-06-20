package com.nuttu.aicloud.model.gateway;

import com.nuttu.aicloud.model.user.Role;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * @author wing
 */
@Data
@Entity
public class DeviceConfig {
    @Id
    @GeneratedValue
    @Getter @Setter private Integer id;
    @Getter @Setter private String owner;
    @Getter @Setter private String version;
    @Getter @Setter private Integer boardConfig;
    @Getter @Setter private String config;
    @Getter @Setter private String upg_path;
    @Getter @Setter private Integer boardUpdate;
    @Column(insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter private Date updatedAt;
    @Column(insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter private Date createdAt;

    public DeviceConfig(){
        this.owner = "";
        this.version = "";
        this.boardUpdate = 0;
        this.config = "~0366H0000;0369H0000.";
        this.upg_path = "";
        this.boardConfig = 0;
    }
    public DeviceConfig(String owner){
        this.owner = owner;
        this.version = "";
        this.boardConfig = 0;
        this.config = "~0366H0000;0369H0000.";
        this.boardUpdate = 0;
    }

}
