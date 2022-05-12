package com.nuttu.aicloud.model.gateway;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
public class Gateway  implements Serializable {
    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid")
    @GeneratedValue(generator="idGenerator")
    @Getter @Setter private String uuid;
    @Getter @Setter private String userId;
    @Getter @Setter private String mac;
    @Getter @Setter private String name;
    @Getter @Setter private String version;
    @Getter @Setter private String address;
    @Getter @Setter private String corp;
    @Getter @Setter private String admin_name;
    @Getter @Setter private String admin_contact;
    @Getter @Setter private String maintain_name;
    @Getter @Setter private String maintain_contact;
    @Getter @Setter private String description;
    @Enumerated(EnumType.STRING)
    @Getter @Setter private GatewayStatus status;
    @JsonIgnore @Getter @Setter private boolean isActive;
    @Column(insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter private Date updatedAt;
    @Column(insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter private Date createdAt;


//    @JsonIgnore @OneToMany
//    @JoinColumn(name = "gatewayMac", referencedColumnName = "mac", insertable = false, updatable = false)
//    private Collection<Status> bs = new ArrayList<Status>();

    public Gateway() {
    }

    public void copyContent(Gateway src) {
        if (src.mac!=null) mac = src.mac;
        if (src.name!=null) name = src.name;
        if (src.version!=null) version = src.version;
        if (src.address!=null) address = src.address;
        if (src.corp!=null) corp = src.corp;
        if (src.admin_name!=null) admin_name = src.admin_name;
        if (src.admin_contact!=null) admin_contact = src.admin_contact;
        if (src.maintain_name!=null) maintain_name = src.maintain_name;
        if (src.maintain_contact!=null) maintain_contact = src.maintain_contact;
        if (src.description!=null) description = src.description;
    }
}
