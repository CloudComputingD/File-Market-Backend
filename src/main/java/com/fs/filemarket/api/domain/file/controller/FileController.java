package com.fs.filemarket.api.domain.file.controller;

import com.fs.filemarket.api.domain.file.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"File Controller"})
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    // 파일 생성
    // 유저의 전체 파일 list
    // 해당 아이디를 가진 파일 정보 반환
    // 파일 검색
    // 파일 삭제 -> 휴지통
    // 파일 좋아요
    // 파일 이름 변경
    // 파일 중복 이름체크
    // 다중파일 업로드
    // trash안에 폴더가 들어가 있는지, 파일이 들어가있는지 .. ?
    private final FileService fileService;

    @ApiOperation("파일을 생성합니다.")
    @PostMapping(value = "")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<String> postFile(@ApiParam(value = "파일 정보")
                                           @Valid @RequestParam String name) {
        return ResponseEntity.status(HttpStatus.CREATED).body()
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
}
