package com.nuttu.aicloud.model.mqtt;

import com.alibaba.fastjson.annotation.JSONField;
import com.nuttu.aicloud.model.mqtt.common.CharTypeConversion;
import lombok.Data;


/**
 * @author wing
 */
@Data
public class BlockLocationSetRequest {
    /**
     * Head	    12	Char	固定字符	    “~NTAINETWORK”
     */
    @JSONField(ordinal = 1)
    private final String head = "~NTAINETWORK";
    /**
     * 用十六进制存储为"64"
     * Type	    1	Byte	消息类型	    十进制 100
     */
    @JSONField(ordinal = 2)
    private final byte type = 100;
    /**
     * Reserve	3	Byte	预留	    0
     */
    @JSONField(ordinal = 3)
    private final String reserve01 = "000";
    /**
     * x	        4	INT	    框图x坐标
     */
    @JSONField(ordinal = 4)
    private int x;
    /**
     * y	        4	INT	    框图y坐标
     */
    @JSONField(ordinal = 5)
    private int y;
    /**
     * With	    4	INT	    框图宽
     */
    @JSONField(ordinal = 6)
    private int with;
    /**
     * High	    4	INT	    框图高
     */
    @JSONField(ordinal = 7)
    private int high;
    /**
     * Reserve	16	Byte	预留	    0
     */
    @JSONField(ordinal = 8)
    private final String reserve02 = "00" + "00" + "00" + "00" + "00" + "00" + "00" + "00";

    public BlockLocationSetRequest(int x, int y, int with, int high) {
        this.x = x;
        this.y = y;
        this.with = with;
        this.high = high;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWith() {
        return with;
    }

    public void setWith(int with) {
        this.with = with;
    }

    public int getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
    }
}
