package com.fs.filemarket.api.domain.file.controller;

import java.io.IOException;
import java.util.List;

import com.fs.filemarket.api.domain.file.File;
import com.fs.filemarket.api.domain.file.dto.FileResponseDto;
import com.fs.filemarket.api.domain.file.enumeration.FileMediaType;
import com.fs.filemarket.api.domain.file.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import lombok.val;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

// spring에서 제공하는 인터페이스로, http 요청에서 업로드된 파일을 나타낸다.
// multipartfile을 사용하여 클라이언트가 전송한 파일을 서버에서 처리하고 저장할 수 있다.

@Tag(name="File", description = "File controller 설명")
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

//    @Operation(summary="s3에 저장된 파일의 리스트를 반환합니다.")
//    @GetMapping(value = "/{bucketName}")
//    public ResponseEntity<?> listFiles(
//            @PathVariable("bucketName") String bucketName
//    ) {
//        val body = fileService.listFiles(bucketName);
//        return ResponseEntity.ok(body);
//    }

    @Operation(summary="s3에 파일을 업로드합니다.")
    @PostMapping(value = "/{bucketName}/upload")
    public ResponseEntity<?> uploadFile(
            @PathVariable("bucketName") String bucketName,
            @RequestHeader(value = "folderId", required = false) Integer folderId,
            @RequestPart("file") List<MultipartFile> multipartFiles
    ) {
        fileService.uploadFile(bucketName, folderId, multipartFiles);
        return ResponseEntity.ok().build();
    }

    @Operation(summary="s3에서 파일을 다운로드합니다.")
    @SneakyThrows
    @GetMapping(value = "/{bucketName}/download/{fileName}")
    public ResponseEntity<?> downloadFile(
            @PathVariable("bucketName") String bucketName,
            @PathVariable("fileName") String fileName
    ) {
        val body = fileService.downloadFile(bucketName, fileName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(FileMediaType.fromFilename(fileName))
                .body(body.toByteArray());
    }

    @Operation(summary="s3에서 파일을 삭제합니다.")
    @DeleteMapping(value = "/{bucketName}/{fileName}")
    public ResponseEntity<?> deleteFile(
            @PathVariable("bucketName") String bucketName,
            @PathVariable("fileName") String fileName
    ) {
        fileService.deleteFile(bucketName, fileName);
        return ResponseEntity.ok().build();
    }

    @Operation(summary="유저의 전체 파일 list를 반환합니다.")
    @GetMapping(value = "/list/{userId}") // list/{userId}로 요청이 들어오면
    public ResponseEntity<List<FileResponseDto.Info>> getAllFile(
            @PathVariable("userId") Integer userId
    ) {
        return ResponseEntity.ok(fileService.getAllFile(userId));
    }

    @Operation(summary="해당 ID의 파일 정보를 가져옵니다.")
    @GetMapping(value = "/{fileId}")
    public ResponseEntity<FileResponseDto.Info> getFileById(@Parameter(description="파일 ID", required = true) @PathVariable final Integer fileId) {
        return ResponseEntity.ok(fileService.getFileById(fileId));
    }

    @Operation(summary="파일을 이름으로 검색합니다.")
    @GetMapping(value="/search")
    public ResponseEntity<List<FileResponseDto.Info>> searchFile(@Parameter(description="파일 이름", required = true) @RequestParam String fileName) {
        return ResponseEntity.ok(fileService.searchFile(fileName));
    }

    @Operation(summary="파일에 좋아요 값을 설정합니다.")
    @PostMapping(value="/favorite/{fileId}")
    public ResponseEntity<Void> favoriteFile(@Parameter(description="파일 ID", required = true) @PathVariable final Integer fileId) {
        fileService.favoriteFile(fileId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @Operation(summary="파일 이름을 변경합니다.")
    @PostMapping(value="/rename/{fileId}")
    public ResponseEntity<String> renameFile(@Parameter(description="파일 ID", required = true) @PathVariable final Integer fileId,
                                             @RequestParam String newName){
        return ResponseEntity.ok(fileService.renameFile(fileId,newName));
    }

    @Operation(summary="파일을 휴지통에 넣습니다.")
    @PostMapping(value="/trash/{fileId}")
    public ResponseEntity<Integer> trashFile(@Parameter(description="파일 ID", required = true) @PathVariable final Integer fileId) {
        return ResponseEntity.ok(fileService.trashFile(fileId));
    }

    @Operation(summary="파일을 휴지통에서 복구합니다.")
    @PostMapping(value="/restore/{fileId}")
    public ResponseEntity<Integer> restoreFile(@Parameter(description="파일 ID", required = true) @PathVariable final Integer fileId) {
        return ResponseEntity.ok(fileService.restoreFile(fileId));
    }

    @Operation(summary="파일을 휴지통에서 완전히 삭제합니다.")
    @DeleteMapping(value="/delete/{fileId}")
    public ResponseEntity<Void> deleteFile(@Parameter(description="파일 ID", required = true) @PathVariable final Integer fileId) {
        fileService.deleteFile(fileId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @Operation(summary="휴지통에 있는 파일을 보여줍니다.")
    @GetMapping(value="/trash/list/{userId}")
    public ResponseEntity<List<FileResponseDto.Info>> getAllTrashFile(@Parameter(description="유저 ID", required = true) @PathVariable final Integer userId){
        return ResponseEntity.ok(fileService.getAllTrashFile(userId));
    }

    @Operation(summary="유저가 지닌 파일의 전체 크기를 보여줍니다.")
    @GetMapping(value="/size/{userId}")
    public ResponseEntity<Integer> getUserFileSize(@Parameter(description="유저 ID", required = true) @PathVariable final Integer userId){
        return ResponseEntity.ok(fileService.getUserFileSize(userId));
    }
}
