package me.soo.helloworld.service;

import me.soo.helloworld.exception.file.FileNotDeletedException;
import me.soo.helloworld.exception.file.FileNotUploadedException;
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
public class LocalFileService implements FileService {

    @Value("${file.upload.directory}")
    private String fileDir;

    public FileData uploadFile(MultipartFile multipartFile, String userId) {
        return createFile(multipartFile, userId);
    }

    public FileData createFile(MultipartFile multipartFile, String userId) {

        try {
            String fileName = getFileName(multipartFile);
            Path filePath = getFilePath(userId);

            Path newFile = filePath.resolve(fileName);
            multipartFile.transferTo(Files.createFile(newFile));

            return new FileData(fileName, String.valueOf(filePath));
        } catch (IOException e) {
            throw new FileNotUploadedException("파일 업로드에 실패하였습니다.", e.getCause());
        }
    }

    public void deleteFile(FileData fileData) {
        try {
            Path path = Paths.get(fileData.getFilePath());
            Path file = path.resolve(fileData.getFileName());

            Files.deleteIfExists(file);

        } catch (IOException e) {
            throw new FileNotDeletedException("파일 삭제에 실패하였습니다.", e.getCause());
        }
    }

    /**
     * String 의 + 연산은 String 의 immutable 한 성격 덕분에 매번 더할 때마다 새로 객체를 생성하며 최종 결과를 제외하고는
     * 모두 GC의 대상이 되기에 메모리 관리에 치명적으로 작용한다.
     *
     * 대형 서버에서는 유저 1명당 1번씩 프로필 파일을 업로드 한다고 가정해도 연산이 발생하는 수는 엄청날 것이다.
     * 따라서 스트링을 사용한 + 연산은 성능에 더 큰 영향을 미칠 것이다.
     *
     * (내용 추가)
     * JDK 1.5? 이후부터는 컴파일러가 자동으로 String +연산을 StringBuilder 와 append 를 사용한 형태로 변형해준다고 한다.
     * 그래도 처음부터 Builder 혹은 Buffer 를 쓰는 것이 좋지 않을까 싶어서 StringBuilder 를 사용하였다.
     *
     * 아래의 파일 이름을 구하는 메소드는 공통된 인스턴스 변수/스태틱 변수에 접근하는 것이 아니기 때문에 멀티 쓰레드 환경이라고 하더라도 동기화 관련
     * 문제가 없을 것이라 판단된다. 따라서 성능이 약간이라도 더 좋은 StringBuilder 를 선택하였다.
     *
     * uuid: 유저들이 올린 파일 이름이 중복되어 저장소에 문제가 생기지 않을까 고민하다가 파일이름을 난수를 이용해 변형하는 방법을 발견하였다.
     * @param multipartFile
     * @return
     */
    private String getFileName(MultipartFile multipartFile) {
        String uuid = UUID.randomUUID().toString();
        String extension = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());

        StringBuilder uniqueFileName = new StringBuilder();
        uniqueFileName.append(uuid).append(".").append(extension);
        return String.valueOf(uniqueFileName);
    }

    private Path getFilePath(String userId) throws IOException {
        // 유저 아이디에 따라 Path 분화 - 한 곳에 파일이 몰려서 저장될 경우 성능 저하가 있다고 한다.
        Path filePath = Paths.get(fileDir, userId).normalize();

        if (!Files.isDirectory(filePath)) {
            Files.createDirectories(filePath);
        }

        return filePath;
    }
}
