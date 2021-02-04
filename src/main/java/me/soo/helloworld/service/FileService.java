package me.soo.helloworld.service;

import me.soo.helloworld.model.file.FileData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileService {

    @Value("${file.upload.directory}")
    private String fileDir;

    public FileData uploadFile(MultipartFile multipartFile, String userId) throws IOException {

        String uniqueFileName = getUniqueFileName(multipartFile);
        String filePath = saveFile(multipartFile, userId, uniqueFileName);

        return new FileData(uniqueFileName, filePath);
    }

    // path, file, multipartFile
    public String saveFile(MultipartFile multipartFile, String userId, String fileName) throws IOException {

        // 디렉토리 분화 - 한 곳에 파일이 몰려서 저장될 경우 성능 저하
        // 변수 이름바꾸기
        Path uploadFile = Paths.get(fileDir, userId);
        Path createFile = uploadFile.resolve(fileName);

        if (!Files.isDirectory(uploadFile)) {
            Files.createDirectories(uploadFile);
        }

        multipartFile.transferTo(Files.createFile(createFile));

        return String.valueOf(uploadFile);
    }

    public void deleteFile() {

    }

    private Path getFilePath(String userId) {
        return null;
    }


    // 일반적으로 파일시스템에 파일저장. 대형 서비스를 목표로 하기 때문에 사용자가 많을 경우 100% 파일이름에 중복이 생길 것이고,
    // 그러면 의도치 않게 파일이 바뀌어 버리는 상황이 나타날 것이다.
    // 이를 어떻게 해결할 수 있을까? uuid 발견 (간단히 말하면 랜덤 이름 생성)

    /**
     * uuid?
     * @param file
     * @return
     */
    private String getUniqueFileName(MultipartFile file) {
        String uuid = UUID.randomUUID().toString();
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());

        // String 의 + 연산은 String 의 immutable 한 성격 덕분에 새로 객체를 생성하기에 비효율적이다.
        // 유저 1명당 1번씩 프로필 파일을 업로드 한다고 가정해도 연산이 발생하는 수는 엄청날 것. 따라서 스트링을 사용해서 + 연산이 발생하면 비효율적
        // StringBuilder(thread not safe) 혹은 StringBuffer(thread safe)를 사용한다.
        // 파일을 업로드 하는 것은 멀티쓰레드 동작과 연관이 있나/있다고 하더라도 실제로 영향을 받을까??
        // 파일을 업로드 하는 것은 값이 지속적으로 업데이트 되는 것이 아니니 쓰레드간 공유도 없을 것 같고 그러면 Bulider 로도 상관 없지 않을까?
        StringBuilder uniqueFileName = new StringBuilder(uuid).append(".").append(extension);
        return String.valueOf(uniqueFileName);
    }





}
