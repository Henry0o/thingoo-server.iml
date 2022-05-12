package com.nuttu.aicloud.model.mqtt;

import com.alibaba.fastjson.annotation.JSONField;
import com.sun.org.apache.xml.internal.serializer.ToStream;
import lombok.Data;

@Data
public class VersionQueryRequest {
    @JSONField(ordinal = 1)
    private final String header = "16" + "56" + "0D";
    @JSONField(ordinal = 2)
    private final String end = "2E";

//    public String toStream(){
//        return header + end;
//    }
}
