package com.nuttu.aicloud.model.session;


import com.nuttu.aicloud.model.response.OperationResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class SessionResponse extends OperationResponse {
  @ApiModelProperty(required = true, value = "")
  private SessionItem data;
}
