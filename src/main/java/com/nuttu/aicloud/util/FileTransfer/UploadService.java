package com.nuttu.aicloud.util.FileTransfer;

import com.nuttu.aicloud.model.response.OperationResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class UploadService {

    public OperationResponse Upload(MultipartFile file) throws Exception{
        OperationResponse resp = new OperationResponse();
        if(file == null || file.isEmpty()){
            resp.setStatus(400);
            resp.setMessage("未正确上传文件");
        }else{
            String filePath = new File("Update_package").getAbsolutePath();
            System.out.println(filePath);
            File fileUpload = new File(filePath);

            if (!fileUpload.exists()) {
                fileUpload.mkdirs();
            }
            fileUpload = new File(filePath,file.getOriginalFilename());
            if(fileUpload.exists()){
                resp.setStatus(415);
                resp.setMessage("文件已存在");
                return resp;
            }
            try{
                file.transferTo(fileUpload);
                System.out.println("文件成功上传");
            } catch (IOException e){
                resp.setStatus(404);
                resp.setMessage("文件上传失败");
                return resp;
            }
            resp.setStatus(200);
            resp.setMessage("文件上传成功");
        }
        return resp;
    }

}
