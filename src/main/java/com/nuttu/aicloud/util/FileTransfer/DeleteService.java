package com.nuttu.aicloud.util.FileTransfer;

import java.io.File;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class DeleteService {
    public boolean Delete(String FileName){
        String filePath = new File("Update_package").getAbsolutePath();
        File delFile = new File(filePath,FileName);
        if(delFile.isFile() && delFile.exists()){
            delFile.delete();
            log.info("删除文件成功:"+FileName);
            return true;
        }else{
            log.info("未找到文件:"+FileName+",删除失败");
            return false;
        }
    }
}
