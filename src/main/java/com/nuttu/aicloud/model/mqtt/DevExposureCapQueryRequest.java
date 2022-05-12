package com.nuttu.aicloud.model.mqtt;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class DevExposureCapQueryRequest {

    /**
     * Head	    12	Char	固定字符	    “~NTAINETWORK”
     */
    @JSONField(ordinal = 1)
    private final String head = "~NTAINETWORK";
    /**
     * Type	    1	Byte	消息类型	    十进制 102
     */
    @JSONField(ordinal = 2)
    private final byte type = 102;
    /**
     * Reserve	3	Byte	  预留	    0
     */
    @JSONField(ordinal = 3)
    private final String reserve = "000";


}
