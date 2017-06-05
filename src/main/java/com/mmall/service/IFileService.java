package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Skye on 2017/6/5.
 */
public interface IFileService {

    String upload(MultipartFile file, String path);
}
