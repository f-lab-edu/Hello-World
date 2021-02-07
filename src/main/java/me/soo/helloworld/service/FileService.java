package me.soo.helloworld.service;

import me.soo.helloworld.model.file.FileData;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    public FileData uploadFile(MultipartFile multipartFile, String userId);

    public FileData createFile(MultipartFile multipartFile, String userId);

    public void deleteFile(FileData fileData);

}
