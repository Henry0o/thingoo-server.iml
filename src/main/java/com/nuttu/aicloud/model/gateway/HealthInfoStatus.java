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
    /**
     * 设备状态标识
     */
    private String status;
}
