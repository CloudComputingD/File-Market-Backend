package com.fs.filemarket.api.domain.file.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;


import com.fs.filemarket.api.domain.file.File;
import com.fs.filemarket.api.domain.file.dto.FileResponseDto;
import com.fs.filemarket.api.domain.user.User;
import com.fs.filemarket.api.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fs.filemarket.api.domain.file.repository.FileRepository;
import com.fs.filemarket.api.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {
    protected final UserService userService;
    protected final FileRepository fileRepository;
    protected final UserRepository userRepository;
    private AmazonS3 s3Client;
//    public void uploadFile(
//            final String bucketName,
//            final String keyName,
//            final Long contentLength,
//            final String contentType,
//            final InputStream value
//    ) throws AmazonClientException {
//        ObjectMetadata metadata = new ObjectMetadata();
//        metadata.setContentLength(contentLength);
//        metadata.setContentType(contentType);
//
//        s3Client.putObject(bucketName, keyName, value, metadata);
//        log.info("File uploaded to bucket({}): {}", bucketName, keyName);
//    }
    public void uploadFile(
            final String bucketName,
            List<MultipartFile> multipartFiles
    ) throws AmazonClientException {
        User user= userService.getCurrentUser();
        multipartFiles.forEach(file -> {
            String fileName = file.getOriginalFilename();
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());
//            log.info("가자");
            try (InputStream inputStream = file.getInputStream()) {
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead);
                s3Client.putObject(putObjectRequest);
                log.info("Content Type: {}", objectMetadata.getContentType());
                log.info("Content Length: {}", objectMetadata.getContentLength());
                log.info("File uploaded to bucket({}): {}", bucketName, fileName);
                File save_file = fileRepository.save(
                        File.builder()
                                .name(fileName)
                                .created_time(LocalDateTime.now())
                                .modified_time(LocalDateTime.now())
                                .user(user)
                                .file_size((int) objectMetadata.getContentLength())
                                .extension(objectMetadata.getContentType())
                                .build()
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    public String createFileName(String fileName){
        return UUID.randomUUID().toString().concat(fileName);
    }

    public ByteArrayOutputStream downloadFile(
            final String bucketName,
            final String keyName
    ) throws IOException, AmazonClientException {
        S3Object s3Object = s3Client.getObject(bucketName, keyName);
        InputStream inputStream = s3Object.getObjectContent();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        int len;
        byte[] buffer = new byte[4096];
        while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        log.info("File downloaded from bucket({}): {}", bucketName, keyName);
        return outputStream;
    }

    public List<String> listFiles(final String bucketName) throws AmazonClientException {
        List<String> keys = new ArrayList<>();
        ObjectListing objectListing = s3Client.listObjects(bucketName);

        while (true) {
            List<S3ObjectSummary> objectSummaries = objectListing.getObjectSummaries();
            if (objectSummaries.isEmpty()) {
                break;
            }
            objectSummaries.stream()
                    .filter(item -> !item.getKey().endsWith("/"))
                    .forEach(summary -> {
                        String key = summary.getKey();
                        long contentLength = summary.getSize();
                    });
            objectListing = s3Client.listNextBatchOfObjects(objectListing);
        }
        log.info("Files found in bucket({}): {}", bucketName, keys);
        return keys;
    }
    public void deleteFile(
            final String bucketName,
            final String keyName
    ) throws AmazonClientException {
        s3Client.deleteObject(bucketName, keyName);
        log.info("File deleted from bucket({}): {}", bucketName, keyName);
    }

    // 여기서부터 db접근
    // getAllFile
    @Transactional(readOnly = true)
    public List<String> getAllFile(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "해당하는 유저가 존재하지 않습니다."
        ));
        return fileRepository.findByUser(user).stream().map(File::getName).collect(Collectors.toList());
    }
    // getFileByID
    @Transactional(readOnly = true)
    public FileResponseDto.Info getFileById(Integer fileId){
        return FileResponseDto.Info.of(fileRepository.findById(fileId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "해당하는 ID를 가진 파일이 존재하지 않습니다."
        )));
    }
    // searchFile
    @Transactional(readOnly = true)
    public List<String> searchFile(String name){
        return fileRepository.findByName(name).stream().map(File::getName).collect(Collectors.toList());
    }
    // favoriteFile
    @Transactional
    public void favoriteFile(Integer folderId) {
        File file = fileRepository.findById(folderId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "해당하는 ID를 가진 파일이 존재하지 않습니다."
        ));
        if(file.isFavorite()){
            file.setFavorite(false);
        }
        else {
            file.setFavorite(true);
        }

        fileRepository.save(file);
    }
    // renameFile
    @Transactional
    public String renameFile(Integer folderId, String newName) {
        File file = fileRepository.findById(folderId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "해당하는 ID를 가진 파일이 존재하지 않습니다."
        ));
        file.setName(newName);
        fileRepository.save(file);

        return newName;
    }
    // trashFile
    @Transactional
    public Integer trashFile(Integer fileId) {
        File file = fileRepository.findById(fileId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "해당하는 ID를 가진 파일이 존재하지 않습니다."
        ));

        file.setTrash(true);
        file.setDeleted_time(LocalDateTime.now());
        fileRepository.save(file);

        return fileId;
    }
    // restoreFile
    @Transactional
    public Integer restoreFile(Integer fileId) {
        File file = fileRepository.findById(fileId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "해당하는 ID를 가진 파일이 존재하지 않습니다."
        ));
        file.setTrash(false);
        file.setDeleted_time(null);
        fileRepository.save(file);

        return fileId;
    }
    // deleteFile
    @Transactional
    public void deleteFile(Integer folderId) {
        File file = fileRepository.findById(folderId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "해당하는 ID를 가진 파일이 존재하지 않습니다."
        ));
        fileRepository.delete(file);
    }
    // getAllTrashFile
    @Transactional(readOnly = true)
    public List<String> getAllTrashFile(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "해당하는 유저가 존재하지 않습니다."
        ));
        return fileRepository.findByUserAndTrash(user).stream().map(File::getName).collect(Collectors.toList());
    }
}