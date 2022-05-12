package com.nuttu.aicloud.model.mqtt.common;

/**
 * @author wing
 */
public class CharTypeConversion {
    public String IntToString(int num){
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(num);
        for (int i = 0; stringBuilder.length() < 8 ; ) {
            stringBuilder.insert(0,"0");
        }
        return stringBuilder.toString();
    }
    public String IntToHexString(int num){
        String res = Integer.toHexString(num).toUpperCase();
        StringBuilder HexSt = new StringBuilder();
        for (int i = 0; i < 4-res.length(); i++) {
            //0转换为ASCII码再转换为16进制字符串
            HexSt.append(Integer.toHexString('0'));
        }
        char[] chars = res.toCharArray();
        for (char aChar : chars) {
            HexSt.append(Integer.toHexString(aChar));
        }
        return HexSt.toString();
    }
    public int HexStr2Int(String HexStr){
        int res = 0;
        for (int i = 0; i < HexStr.length(); i++) {
            res += Math.pow(16,HexStr.length()-i-1)*(HexStr.charAt(i)-'0');
        }
        return res;
    }
}
