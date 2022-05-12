package com.nuttu.aicloud.model.mqtt;

import com.alibaba.fastjson.annotation.JSONField;
import com.nuttu.aicloud.model.mqtt.common.CharTypeConversion;
import lombok.Data;

@Data
public class OptimalExposireParamSetRequest {
    /**
     * Head	    12	Char	固定字符	    “~NTAINETWORK”
     */
    @JSONField(ordinal = 1)
    private final String head = "~NTAINETWORK";
    /**
     * Type	    1	Byte	消息类型	    十进制 104
     */
    @JSONField(ordinal = 2)
    private final byte type = 106;
    /**
     * Reserve	3	Byte	预留	    0
     */
    @JSONField(ordinal = 3)
    private final String reserve01 = "00" + "00" + "00";
    /**
     * Exp	    4	INT	    曝光时间
     */
    @JSONField(ordinal = 4)
    private int exp;
    /**
     * DGain	    4	INT	    数字增益
     */
    @JSONField(ordinal = 5)
    private int dGain;
    /**
     * AGain	    4	INT	    模拟增益
     */
    @JSONField(ordinal = 6)
    private int again;
    /**
     * Reserve	16	Byte	预留	    0
     */
    @JSONField(ordinal = 7)
    private final String reserve02 = "00" + "00" + "00" + "00" + "00" + "00" + "00" + "00";

    public OptimalExposireParamSetRequest(int exp, int dGain, int again) {
        this.exp = exp;
        this.dGain = dGain;
        this.again = again;
    }
}
