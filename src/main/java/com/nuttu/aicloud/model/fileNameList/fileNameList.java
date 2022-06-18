package com.nuttu.aicloud.model.fileNameList;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * @author wing
 */
@Data
@Entity
public class fileNameList implements Serializable {
    @Id
    @GeneratedValue
    @Getter @Setter private Integer id;
    @Getter @Setter private String version;
    @Getter @Setter private String fileName;
    @Getter @Setter private String aliasName;

    @Column(insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter private Date updatedAt;
    @Column(insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter private Date createdAt;

}
