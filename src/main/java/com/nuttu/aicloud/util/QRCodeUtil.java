package com.nuttu.aicloud.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Hashtable;

public class QRCodeUtil {
    /**
     * 生成二维码图片并返回
     * @param content 二维码存储的内容
     * @param width   指定二维码的宽度
     * @param height  指定二维码的高度
     * @return
     * @throws Exception
     */
    public static BufferedImage createQrCodeImage(String content, int width, int height) throws Exception{
        Hashtable hints = new Hashtable();
        //指定纠错等级
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        //生成二维码矩阵
        BitMatrix bitMatrix =
                new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        //获取矩阵宽度、高度,生成二维码图片
        BufferedImage image = new BufferedImage(bitMatrix.getWidth(),bitMatrix.getHeight(),BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }

        return image;
    }

    /**
     * BufferedImage 编码转换为 base64
     * @param bufferedImage
     * @return
     **/
    public static String BufferedImageToBase64(BufferedImage bufferedImage) {
        //io流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            //写入流中
            ImageIO.write(bufferedImage, "png", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //转换成字节
        byte[] bytes = baos.toByteArray();
        BASE64Encoder encoder = new BASE64Encoder();
        //转换成base64串
        String png_base64 = encoder.encodeBuffer(bytes).trim();
        //删除 \r\n
        png_base64 = png_base64.replaceAll("\n", "").replaceAll("\r", "");
        return "data:image/png;base64," + png_base64;
    }

}
