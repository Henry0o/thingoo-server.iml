package com.nuttu.aicloud.model;

import lombok.Data;

@Data  // For Getters and Setters
public class VersionModel  {
  private String version = null;
  private Integer major = null;
  private Integer minor = null;
  private Integer patch = null;
}
