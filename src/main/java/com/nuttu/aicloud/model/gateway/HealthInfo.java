package com.nuttu.aicloud.model.gateway;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
public class HealthInfo implements Serializable {
    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid")
    @GeneratedValue(generator="idGenerator")
    @Getter @Setter private String uuid;
    @Getter @Setter private String sn;
    @Getter @Setter private String version;
    @Getter @Setter private String owner;
    @Getter @Setter private String type;
    @Getter @Setter private String id_card;
    @Getter @Setter private String name;
    @Getter @Setter private String phone;
    @Getter @Setter private String code_color;
    @Getter @Setter private String photo;
    @Getter @Setter private String photo_time;
    @Getter @Setter private String pcr_igmc;
    @Getter @Setter private String pcr_time;
    @Getter @Setter private String pcr_result;
    @Getter @Setter private Integer vacc;
    @Getter @Setter private String vacc_date;
    @Getter @Setter private String qr_show;
    @Getter @Setter private String qr_create;
    @Getter @Setter private String trip;

    @Getter @Setter private String qr_content;
    @Column(insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter private Date updatedAt;
    @Column(insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter private Date createdAt;

    public HealthInfo(){
    }
}
