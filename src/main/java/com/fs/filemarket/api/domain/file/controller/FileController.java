package com.fs.filemarket.api.domain.file.controller;

import java.io.IOException;
import java.util.List;

import com.fs.filemarket.api.domain.file.File;
import com.fs.filemarket.api.domain.file.enumeration.FileMediaType;
import com.fs.filemarket.api.domain.file.service.FileService;
import lombok.SneakyThrows;
import lombok.val;
import lombok.RequiredArgsConstructor;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

// spring에서 제공하는 인터페이스로, http 요청에서 업로드된 파일을 나타낸다.
// multipartfile을 사용하여 클라이언트가 전송한 파일을 서버에서 처리하고 저장할 수 있다.

@Api(tags = {"File Controller"})
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {
    // 파일 업로드
    // 다중파일 업로드
    // 파일 다운로드
    // 유저의 전체 파일 list
    // 해당 아이디를 가진 파일 정보 반환
    // 파일 검색
    // 파일 삭제 -> 휴지통
    // 파일 좋아요
    // 파일 이름 변경
    // 파일 중복 이름체크
    // trash안에 폴더가 들어가 있는지, 파일이 들어가있는지 .. ?
    private final FileService fileService;
    @ApiOperation("s3에 저장된 파일의 리스트를 반환합니다.")
    @GetMapping("/{bucketName}")
    public ResponseEntity<?> listFiles(
            @PathVariable("bucketName") String bucketName
    ) {
        val body = fileService.listFiles(bucketName);
        return ResponseEntity.ok(body);
    }

    // 다중업로드 해야함
    // 메타데이터 보내바야함
    @ApiOperation("s3에 파일을 업로드합니다.")
    @PostMapping("/{bucketName}/upload")
    public ResponseEntity<?> uploadFile(
            @PathVariable("bucketName") String bucketName,
            @RequestPart("file") List<MultipartFile> multipartFiles
    ) {
        fileService.uploadFile(bucketName, multipartFiles);
        return ResponseEntity.ok().build();
    }
//    @PostMapping("/{bucketName}/upload")
//    @SneakyThrows(IOException.class)
//    public ResponseEntity<?> uploadFile(
//            @PathVariable("bucketName") String bucketName,
//            @RequestPart("file") MultipartFile file,
//            @RequestPart("fileName") String fileName
//    ) {
//        fileService.uploadFile(bucketName, fileName, file.getSize(), file.getContentType(), file.getInputStream());
//        return ResponseEntity.ok().build();
//    }


    @ApiOperation("s3에서 파일을 다운로드합니다.")
    @SneakyThrows
    @GetMapping("/{bucketName}/download/{fileName}")
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

    @ApiOperation("s3에서 파일을 삭제합니다.")
    @DeleteMapping("/{bucketName}/{fileName}")
    public ResponseEntity<?> deleteFile(
            @PathVariable("bucketName") String bucketName,
            @PathVariable("fileName") String fileName
    ) {
        fileService.deleteFile(bucketName, fileName);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("유저의 전체 파일 list를 반환합니다.")
    @GetMapping(value = "/list/{userId}") // list/{userId}로 요청이 들어오면
    public ResponseEntity<List<String>> getAllFile(@ApiParam(value = "유저 ID", required = true) @PathVariable final Integer userId) {
        return ResponseEntity.ok(fileService.getAllFile(userId));
    }
    @ApiOperation("해당 ID의 파일 정보를 가져옵니다.")
    @GetMapping(value = "/{fileId}}")
    public ResponseEntity<FileResponseDto.Info> getFileById(@ApiParam(value="파일 ID", required = true) @PathVariable final Integer fileId) {
        return ResponseEntity.ok(fileService.getFolderById(fileId));
    }
    @ApiOperation("파일을 이름으로 검색합니다.")
    @GetMapping(value="/search")
    public ResponseEntity<List<String>> searchFile(@ApiParam(value="파일 이름", required = true) @RequestParam String fileName) {
        return ResponseEntity.ok(fileService.searchFile(fileName));
    }

    @ApiOperation("파일에 좋아요 값을 설정합니다.")
    @PostMapping(value="/favorite/{folderId}")
    public ResponseEntity<Void> favoriteFile(@ApiParam(value="파일 ID", required = true) @PathVariable final Integer folderId) {
        fileService.favoriteFile(fileId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @ApiOperation("파일 이름을 변경합니다.")
    @PostMapping(value="/rename/{fileId}")
    public ResponseEntity<String> renameFile(@ApiParam(value="파일 ID", required = true) @PathVariable final Integer fileId,
                                               @RequestParam String newName){
        return ResponseEntity.ok(fileService.renameFile(fileId,newName));
    }

    @ApiOperation("파일을 휴지통에 넣습니다.")
    @PostMapping(value="/trash/{fileId}")
    public ResponseEntity<Integer> trashFile(@ApiParam(value="파일 ID", required = true) @PathVariable final Integer fileId) {
        return ResponseEntity.ok(fileService.trashFile(fileId));
    }

    @ApiOperation("파일을 휴지통에서 복구합니다.")
    @PostMapping("value=/restore/{fileId}")
    public ResponseEntity<Integer> restoreFile(@ApiParam(value="파일 ID", required = true) @PathVariable final Integer fileId) {
        return ResponseEntity.ok(fileService.restoreFile(fileId));
    }

    @ApiOperation("파일을 휴지통에서 완전히 삭제합니다.")
    @ApiOperation("value=/delete/{fileId}")
    public ResponseEntity<Void> deleteFile(@ApiParam(value="파일 ID", required = true) @PathVariable final Integer fileId) {
        fileService.deleteFile(fileId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @ApiOperation("휴지통에 있는 파일을 보여줍니다.")
    @GetMapping("value=/trash/list/{userId}")
    public ResponseEntity<List<String>> getAllTrashFile(@ApiParam(value="유저 ID", required = true) @PathVariable final Integer userId){
        return ResponseEntity.ok(fileService.getAllTrashFile(userId));
    }
}