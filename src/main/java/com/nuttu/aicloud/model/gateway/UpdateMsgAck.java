package com.nuttu.aicloud.model.gateway;

import lombok.Data;

/**
 * @author wing
 */
@Data
public class UpdateMsgAck {
    /**
     * 原样返回配置接口中的事务 id
     */
    private String sn;
    /**
     * 扫码器序列号
     */
    private Integer transaction;
    /**
     * 0x06：成功
     * 0x15：失败
     */
    private Integer code;
    /**
     * 返回信息提示
     */
    private String message;
}
