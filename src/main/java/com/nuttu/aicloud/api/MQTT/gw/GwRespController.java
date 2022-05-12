package com.nuttu.aicloud.api.MQTT.gw;

import com.nuttu.aicloud.model.device.ExpCap;
import com.nuttu.aicloud.model.mqtt.common.CharTypeConversion;
import com.nuttu.aicloud.model.response.OperationResponse;
import com.nuttu.aicloud.model.status.Status;
import com.nuttu.aicloud.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wing
 */
@RestController
public class GwRespController {
    @Autowired
    StatusRepository statusRepository;
    @RequestMapping(value = "/account/gateway/getresp",method = RequestMethod.GET)
    public OperationResponse respReader(@RequestParam(value = "gatewayMac") String gatewayMac,
                                        @RequestParam(value = "mac") String mac){
        OperationResponse response = new OperationResponse();
        List<Status> statusList = statusRepository.findAllByGatewayMacAndMacIn(gatewayMac, mac);
        String rawData = statusList.get(0).getRawData();
        response = rawDataDecode(rawData);
        return response;
    }
    public OperationResponse rawDataDecode(String rawData){
        OperationResponse resp = new OperationResponse();
        CharTypeConversion typeConversion = new CharTypeConversion();
        if(rawData.endsWith("06")){
            if (rawData.startsWith("3030364348")){
                //视频流帧率设置响应
                String paramStr = rawData.substring(10,rawData.length()-2);
                //TODO:解码视频帧率设置为多少，返回字符串“视频帧率成功设置为**”
                resp.setStatus(200);
                //TODO:没写完
                int paramInt = parseStr2int(paramStr);
                resp.setMessage("视频帧率成功设置为"+paramInt+"FPS");
            }else{
                //设备客户端版本查询响应
                //TODO:将其转换为对应UTF-8字符串并返回
                resp.setStatus(200);
                resp.setMessage("为对应UTF-8字符串");
            }
        }
        else if (rawData.startsWith("~NTAINETWORK")){
            String type = rawData.substring(12,14);
            int decType = typeConversion.HexStr2Int(type);
            //Type对应十进制值为101时，为：云端框图位置设置响应
            //Type对应十进制值为103时，为：设备曝光能力查询响应
            //Type对应十进制值为105时，为：曝光参数设置响应
            //Type对应十进制值为107时，为：最优曝光参数设置响应
            //Type对应十进制值为108时，为：图像信息
            switch (decType){
                case 101:
                    System.out.println("云端框图位置设置响应");
                    //消息格式12+1+3（预留位）+ 4（结果）
                    if ("0000".equals(rawData.substring(rawData.length()-4))){
                        resp.setStatus(200);
                        resp.setMessage("云端框图位置设置成功");
                        break;
                    }else {
                        resp.setStatus(400);
                        resp.setMessage("云端框图位置设置失败");
                        break;
                    }


                case 103:
                    System.out.println("曝光查询");


                    break;
                case 105:
                    System.out.println("曝光参数设置响应");
                    break;
                case 107:
                    System.out.println("最有曝光参数设置响应");
                    break;
                case 108:
                    System.out.println("图像信息");
                default:
                    System.out.println("未知信息");
            }


        }
        return resp;
    }
    public int parseStr2int(String param){
        if (param.length()!=8){
            return -1;
        }
        char[] chars = new char[4];
        int res = 0;
        for (int i = 0; i < param.length(); i++) {

            if(param.charAt(i)>=48 && param.charAt(i)<=57){
                //'0'-'9'
                chars[i/2] +=(int)(((i%2==0)?16:1) * (param.charAt(i)-48));
            }else if(param.charAt(i)>=65 && param.charAt(i)<=90){
                //'A'-'Z'
                chars[i/2] +=(int)(((i%2==0)?16:1) * (param.charAt(i)-55));
            } else if (param.charAt(i) >= 97 && param.charAt(i) <= 122) {
                //'a'-'z'
                chars[i/2] +=(int)(((i%2==0)?16:1) * (param.charAt(i)-87));
            }
        }
        for (int i = 0; i < chars.length; i++) {
            if (chars[i]<='9' && chars[i]<='0'){
                res += (chars[i]-'0')*Math.pow(16,chars.length-i-1);
            }else if (chars[i]<='A' && chars[i]<='Z'){
                res += (chars[i]-'A'+10)*Math.pow(16,chars.length-i-1);
            }else if (chars[i]<='a' && chars[i]<='z'){
                res += (chars[i]-'a' +10)*Math.pow(16,chars.length-i-1);
            }
        }
        return res;
    }

}

