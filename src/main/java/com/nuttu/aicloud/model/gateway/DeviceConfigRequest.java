package com.nuttu.aicloud.model.gateway;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

/**
 * @author wing
 */
@Data
public class DeviceConfigRequest {

    private Integer transaction;
    @JsonInclude(Include.NON_NULL)
    private String config;
    @JsonInclude(Include.NON_NULL)
    private String new_version;
    @JsonInclude(Include.NON_NULL)
    private String upg_url;
}
