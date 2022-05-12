package com.nuttu.aicloud.util.FileTransfer;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

public class DownloadService {
    public void logDownload(String name, HttpServletResponse response) throws Exception {
        File file = new File("Update_package" + File.separator + name);

        if (!file.exists()) {
            System.out.println(name + "文件不存在");
            System.out.println(file);
        }
        response.setContentType("application/force-download");
        response.addHeader("Content-Disposition", "attachment;fileName=" + name);

        byte[] buffer = new byte[1024];
        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis)) {

            OutputStream os = response.getOutputStream();

            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
        }
    }
}
