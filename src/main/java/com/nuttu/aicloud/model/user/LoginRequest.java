package com.nuttu.aicloud.model.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LoginRequest {

  @ApiModelProperty(example = "demo@nuttu.com", required = true)
  private String username = "";

  @ApiModelProperty(example = "demo", required = true)
  private String password = "";

}
