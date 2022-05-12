package com.nuttu.aicloud;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nuttu.aicloud.model.mqtt.common.CharTypeConversion;
import com.nuttu.aicloud.model.user.User;
import com.nuttu.aicloud.util.QRCodeUtil;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class StringParse {

    @Test
    public void index() {
        stu s1 = new stu("aaa",1,"男");
        stu s2 = new stu("bbb",2,"女");
        stu s3 = new stu("ccc",3,"男");
        List<stu> stus = new ArrayList<>();
        stus.add(s1);
        stus.add(s2);
        stus.add(s3);

        System.out.println("-------------------java对象转JSON字符串---------------------");
        String str1 = JSON.toJSONString(stus);
        System.out.println("JSON.toJSONString(stus)====>"+str1);
        String str2 = JSON.toJSONString(s1);
        System.out.println("JSON.toJSONString(s1)====>"+str2);

        System.out.println("-------------------JSON字符串转java对象---------------------");
        stu s_res = JSON.parseObject(str2,stu.class);
        System.out.println(s_res.getSex());
        List<stu> stus_res = new ArrayList<>();

        JSONArray array_stu = JSON.parseArray(str1);
        System.out.println(array_stu);
        stu arrayStu01 = array_stu.getObject(0, stu.class);
        System.out.println(arrayStu01.getName());



    }

    @Test
    public void TestSubString(){
        String str = "123456789";
        System.out.println(str.substring(str.length()-4));
    }
    @Test
    public void ParseStr2int(){
        String Param = "30303041";
        if (Param.length()!=8){
            System.out.println("太长了");
        }
        char[] chars = new char[4];
        int res = 0;
        for (int i = 0; i < Param.length(); i++) {

            if(Param.charAt(i)>=48 && Param.charAt(i)<=57){
                //'0'-'9'
                chars[i/2] +=(int)(((i%2==0)?16:1) * (Param.charAt(i)-48));
            }else if(Param.charAt(i)>=65 && Param.charAt(i)<=90){
                //'A'-'Z'
                chars[i/2] +=(int)(((i%2==0)?16:1) * (Param.charAt(i)-55));
            } else if (Param.charAt(i) >= 97 && Param.charAt(i) <= 122) {
                //'a'-'z'
                chars[i/2] +=(int)(((i%2==0)?16:1) * (Param.charAt(i)-87));
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
        System.out.println(res);

    }
    @Test
    public void CreateQR(){
        String test = "~{\"address\":\"深圳\",\"owner\":\"265@qq.com\",\"contact\":\"胡总\",\"phone\":\"13812345678\",\"RegisterServerAdd\":\"http://192.168.1.83:8080/gateways/addHealthCode\",\"DataServerAdd\":\"https://cloud.jtymedia.com/api/health/postHealthData\"}.";
        System.out.println(test.hashCode());

    }


}

