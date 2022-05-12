package com.nuttu.aicloud.model.gateway;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@Entity
public class HealthCode implements Serializable {
    @Id
    @Getter @Setter private String sn;
    @Getter @Setter private String version;
    @Getter @Setter private String address;
    @Getter @Setter private String owner;
    @Getter @Setter private String contact;
    @Getter @Setter private String phone;

}
