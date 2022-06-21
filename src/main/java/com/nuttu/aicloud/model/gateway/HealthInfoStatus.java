package com.nuttu.aicloud.model.gateway;

import lombok.Data;

/**
 * @author wing
 */
@Data
public class HealthInfoStatus {
    /**
     * 扫码器序列号
     */
    private String sn;
    private String version;
    private String PN;
    private String SN;
    private String ICCID;
    /**
     * 设备状态标识
     */
    private String status;
}
