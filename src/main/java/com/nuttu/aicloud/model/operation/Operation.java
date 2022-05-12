package com.nuttu.aicloud.model.operation;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
public class Operation  implements Serializable {
        @Id
        @GenericGenerator(name="idGenerator", strategy="uuid")
        @GeneratedValue(generator="idGenerator")
        @Getter @Setter private String uuid;
        @Getter @Setter private String userId;
        @Getter @Setter private String gateway_mac;
        @Getter @Setter private String operation;
        @Getter @Setter private String responseCode;
        @Getter @Setter private String responseMessage;
        @Column(insertable = false, updatable = false)
        @Temporal(TemporalType.TIMESTAMP)
        @Getter @Setter private Date updatedAt;
        @Column(insertable = false, updatable = false)
        @Temporal(TemporalType.TIMESTAMP)
        @Getter @Setter private Date createdAt;

        @Override
        public String toString() {
                return "Operation{" +
                        "uuid='" + uuid + '\'' +
                        ", userId='" + userId + '\'' +
                        ", gateway_mac='" + gateway_mac + '\'' +
                        ", operation='" + operation + '\'' +
                        ", responseCode='" + responseCode + '\'' +
                        ", responseMessage='" + responseMessage + '\'' +
                        ", updatedAt=" + updatedAt +
                        ", createdAt=" + createdAt +
                        '}';
        }
}
