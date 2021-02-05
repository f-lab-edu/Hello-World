package me.soo.helloworld.service;

import me.soo.helloworld.model.file.FileData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {

    @Value("${file.upload.directory}")
    private String fileDir;


    public FileData uploadFile(MultipartFile multipartFile, String userId) throws IOException {
        return createFile(multipartFile, userId);
    }

    public FileData createFile(MultipartFile multipartFile, String userId) throws IOException {

        String fileName = getFileName(multipartFile);
        Path filePath = getFilePath(userId);

        Path newFile = filePath.resolve(fileName);
        multipartFile.transferTo(Files.createFile(newFile));

        return new FileData(fileName, String.valueOf(filePath));
    }

    public void deleteFile(FileData fileData) throws IOException {
        Path path = Paths.get(fileData.getFilePath());
        Path file = path.resolve(fileData.getFileName());

        Files.deleteIfExists(file);
    }

    private Path getFilePath(String userId) throws IOException {
        // 유저 아이디에 따라 Path 분화 - 한 곳에 파일이 몰려서 저장될 경우 성능 저하가 있다고 함
        Path filePath = Paths.get(fileDir, userId);

        if (!Files.isDirectory(filePath)) {
            Files.createDirectories(filePath);
        }

        return filePath;
    }

    /**
     * String 의 + 연산은 String 의 immutable 한 성격 덕분에 새로 객체를 생성하기에 비효율적이다.
     * 유저 1명당 1번씩 프로필 파일을 업로드 한다고 가정해도 연산이 발생하는 수는 엄청날 것. 따라서 스트링을 사용해서 + 연산이 발생하면 비효율적
     * 컴파일러가 자동으로 변형해준다고 해도 처음부터 Builder/Buffer를 쓰는 것이 좋아보인다.
     * uuid: 유저들이 올린 파일 이름이 중복되어 문제가 생기는 것을 방지하기 위함(의도치 않게 저장소에 있는 파일이 삭제되거나 변형되는 등)
     * @param multipartFile
     * @return
     */
    private String getFileName(MultipartFile multipartFile) {
        String uuid = UUID.randomUUID().toString();
        String extension = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());

//        if (extension == null) return null; 필수인가?

        StringBuilder uniqueFileName = new StringBuilder();
        uniqueFileName.append(uuid).append(".").append(extension);
        return String.valueOf(uniqueFileName);
    }
}
