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
public class HealthIndoModel implements Serializable {
    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid")
    @GeneratedValue(generator="idGenerator")
    @Getter
    @Setter
    private String uuid;
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
    @Getter @Setter private String address;

    public HealthIndoModel(){
    }
    public HealthIndoModel(String uuid,String owner,String address,String version,String sn,String type,String name,String code_color,String pcr_result){
        this.setUuid(uuid);
        this.setOwner(owner);
        this.setVersion(version);
        this.setSn(sn);
        this.setType(type);
        this.setName(name);
        this.setCode_color(code_color);
        this.setPcr_result(pcr_result);
        this.setAddress(address);
    }
    public HealthIndoModel(String uuid,
                      String sn,
                      String version,
                      String owner,
                      String type,
                      String id_card,
                      String name,
                      String phone,
                      String code_color,
                      String photo,
                      String photo_time,
                      String pcr_igmc,
                      String pcr_time,
                      String pcr_result,
                      Integer vacc,
                      String vacc_date,
                      String qr_show,
                      String qr_create,
                      String trip,
                      String qr_content){
        this.setUuid(uuid);
        this.setSn(sn);
        this.setVersion(version);
        this.setOwner(owner);
        this.setType(type);
        this.setId_card(id_card);
        this.setName(name);
        this.setPhone(phone);
        this.setCode_color(code_color);
        this.setPhoto(photo);
        this.setPhoto_time(photo_time);
        this.setPcr_igmc(pcr_igmc);
        this.setPcr_time(pcr_time);
        this.setPcr_result(pcr_result);
        this.setVacc(vacc);
        this.setVacc_date(vacc_date);
        this.setQr_show(qr_show);
        this.setQr_create(qr_create);
        this.setTrip(trip);
        this.setQr_content(qr_content);
    }
}
