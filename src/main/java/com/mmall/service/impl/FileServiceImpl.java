package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Skye on 2017/6/5.
 */
@Service("iFileService")
public class FileServiceImpl implements IFileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    public String upload(MultipartFile file, String path) {
        //上传文件的原始文件名
        String fileName = file.getOriginalFilename();
        //扩展名 例如：abc.jpg 取.后面的jpg
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        //使用UUID生成上传文件的新文件名
        String uploadFileName = UUID.randomUUID().toString() + "." + fileExtensionName;

        logger.info("开始上传文件，上传文件的文件名：{},上传的路径是{}，新文件名:{}", fileName, path
                , uploadFileName);

        //在tomcat发布项目目录下创建“path”文件夹（控制层发来path参数：upload）
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            //赋予可写权限
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }

        File targetFile = new File(path, uploadFileName);
        try {
            // springmvc保存文件到指定的路径
            file.transferTo(targetFile);
            // 将targetFile 上传到FTP服务器
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            // 删除upload下面的文件
            targetFile.delete();
        } catch (IOException e) {
            logger.error("上传文件异常", e);
            return null;
        }
        return targetFile.getName();
    }
}
