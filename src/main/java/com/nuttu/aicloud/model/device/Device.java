package com.nuttu.aicloud.model.device;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
public class Device  implements Serializable {
    public final static int defaultBasePower = 52;

    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid")
    @GeneratedValue(generator="idGenerator")
    @Getter @Setter private String uuid;
    @Getter @Setter private String userId;
    @Getter @Setter private String mac;
    @Getter @Setter private String name;
    @Enumerated(EnumType.STRING)
    @Getter @Setter private DeviceType type;
    @Getter @Setter private String model;
    @Getter @Setter private String profile;
    @Getter @Setter private String capSet;
    @Getter @Setter private String description;
    @Column(insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter private Date updatedAt;
    @Column(insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter private Date createdAt;

    public Device() {
    }
}
