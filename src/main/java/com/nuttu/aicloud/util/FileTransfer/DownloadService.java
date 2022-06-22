package com.nuttu.aicloud.util.FileTransfer;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

public class DownloadService {
    public void logDownload(String name, HttpServletResponse response) throws Exception {
        File file = new File("Update_package" + File.separator + name);
        if (!file.exists()) {
            System.out.println(name + "文件不存在");
            System.out.println(file);
        }
//        response.setContentType("application/force-download");
//        response.addHeader("Content-Disposition", "attachment;fileName=" + name);

//        byte[] buffer = new byte[1024];
//        try (FileInputStream fis = new FileInputStream(file);
//             BufferedInputStream bis = new BufferedInputStream(fis)) {
//
//            OutputStream os = response.getOutputStream();
//
//            int i = bis.read(buffer);
//            while (i != -1) {
//                os.write(buffer, 0, i);
//                i = bis.read(buffer);
//            }
//        }
        try(FileInputStream fileInputStream = new FileInputStream(file);
            InputStream fis = new BufferedInputStream(fileInputStream);){
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();

            // 清空response
            response.reset();
            // 设置response的Header
            response.setCharacterEncoding("UTF-8");
            //Content-Disposition的作用：告知浏览器以何种方式显示响应返回的文件，用浏览器打开还是以附件的形式下载到本地保存
            //attachment表示以附件方式下载   inline表示在线打开   "Content-Disposition: inline; filename=文件名.mp3"
            // filename表示文件的默认名称，因为网络传输只支持URL编码的相关支付，因此需要将文件名URL编码后进行传输,前端收到后需要反编码才能获取到真正的名称
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(name, "UTF-8"));
            // 告知浏览器文件的大小
            response.addHeader("Content-Length", "" + file.length());
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            outputStream.write(buffer);
            outputStream.flush();
        }
    }
}
