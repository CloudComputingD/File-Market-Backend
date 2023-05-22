package com.fs.filemarket.api.domain.file.service;

import com.fs.filemarket.api.domain.file.File;
import com.fs.filemarket.api.domain.file.repository.FileRepository;
import com.fs.filemarket.api.domain.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {
    protected final FileRepository fileRepository;
    private final UserService userService;
    private final String uploadPath = "";

    @Transactional
    public String uploadFileToS3(MultipartFile file) {
        try {
            String originalFileName = file.getOriginalFilename();
            String savedFileName = UUID.randomUUID().toString() + "_" + originalFileName;
            File savedFile = new File(uploadPath + savedFileName);
            file.transferTo(savedFile);

            String fileUrl = s3Service.uploadFile(savedFile); // S3에 파일 업로드 후 URL 반환

            File fileUploadEntity = new File(originalFileName, savedFileName, fileUrl);
            fileRepository.save(fileUploadEntity); // 파일 URL을 repository로 전달하여 저장

            return fileUrl;
        } catch (IOException e) {
            return "Error occurred while uploading file.";
        }
    }


    @Transactional(readOnly = true)
    public List<String> getAllFile(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "해당하는 유저가 존재하지 않습니다."
        ));
        return fileRepository.findByUser(user).stream().map();
    }
}
