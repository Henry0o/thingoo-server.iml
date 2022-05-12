package com.nuttu.aicloud.model.mqtt;

import com.alibaba.fastjson.annotation.JSONField;
import com.nuttu.aicloud.model.mqtt.common.CharTypeConversion;
import lombok.Data;

@Data
public class fpsSetRequest {
    /**
     * 命令头  3字节 BYTE 固定字符 16进制 16 4D 0D
     */
    @JSONField(ordinal = 1)
    private final String header = "16"+"4D"+"0D";
    /**
     * 命令    4字节 Byte 命令类型	16进制30 30 36 43
     */
    @JSONField(ordinal = 2)
    private final String command = "30"+"30"+"36"+"43";
    /**
     * 命令参数类型	1	Byte	命令参数类型	16进制48（对应参数为16进制）
     */
    @JSONField(ordinal = 3)
    private final String type = "48";
    /**
     * 命令参数	4	Byte	16进制数字对应的ASCII码	比如，设置视频流帧率为10，需要填写”000A”,16进制 30 30 30 41
     */
    @JSONField(ordinal = 4)
    private String orderParam;
    //命令结束符	1	Byte	固定字符	16进制2e
    @JSONField(ordinal = 5)
    private final String end = "2E";


//    public String toStream(int fps){
//        CharTypeConversion charTypeConversion = new CharTypeConversion();
//        return header + command + type + charTypeConversion.IntToHexString(fps) + end;
//    }


    public fpsSetRequest(int fps) {
        CharTypeConversion charTypeConversion = new CharTypeConversion();
        this.orderParam = charTypeConversion.IntToHexString(fps);
    }
}
