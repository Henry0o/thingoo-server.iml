package com.nuttu.aicloud.mqtt.config;

/**
 * @author wing
 */
public enum PrefixAndSuffix {
    // Topic的前缀为"/gw/"
    PRE_GW("/gw/"),
    PRE_DEV("/dev/"),
    // 设备状态的Topic后缀
    SUF_STATUS("/status"),
    // 设备注册的Topic后缀
    SUF_REGISTER("/register"),
    // 版本查询的Topic后缀
    SUF_VER_QUERY("/ver_query"),
    // 视频帧率设置的Topic后缀
    SUF_SET_FPS("/set_fps"),
    // 云端框图位置设置的Topic后缀
    SUF_SET_LOCATION("/set_location"),
    // 设备曝光能力查询的Topic后缀
    SUF_EXPCAP_QUERY("/expcap_query"),
    // 曝光参数设置的Topic后缀
    SUF_EXPPARAM_SET("/expparam_set"),
    // 最优曝光参数设置的Topic后缀
    SUF_OPTEXP_SET("/optexp_set")
    ;

    private final String value;
    PrefixAndSuffix(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
