package com.nuttu.aicloud;

import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

public class stu {
    @Getter @Setter
    private String name;
    @Getter @Setter
    private Integer id;
    @Getter @Setter
    private String sex;

    public stu() {
    }

    public stu(String name, Integer id, String sex) {
        this.name = name;
        this.id = id;
        this.sex = sex;
    }
}
