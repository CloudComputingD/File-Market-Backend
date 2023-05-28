package com.fs.filemarket.api.domain.folder.controller;

import com.fs.filemarket.api.domain.folder.dto.FolderResponseDto;
import com.fs.filemarket.api.domain.folder.service.FolderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"Folder Controller"})
@RestController
@RequestMapping("/folder")
@RequiredArgsConstructor
public class FolderController {
    //유저의 전체 폴더 list
    //폴더 생성
    //해당 아이디를 가진 폴더 정보 반환
    //폴더 검색
    //해당 폴더에 속한 모든 파일 불러오기
    //폴더 삭제 -> 휴지통
    //폴더 좋아요
    //폴더 이름 변경
    //##부분 수정(dto)

    private final FolderService folderService;

    @ApiOperation("폴더를 생성합니다.")
    @PostMapping(value = "")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<String> postFolder(@ApiParam(value = "폴더 정보")
                                           @Valid @RequestParam String name) {
        return ResponseEntity.status(HttpStatus.CREATED).body("/folder/" + folderService.postFolder(name));
    }

    @ApiOperation("유저의 전체 폴더 list를 반환합니다.")
    @GetMapping(value = "/list/{userId}")
    public ResponseEntity<List<String>> getAllFolder(@ApiParam(value = "유저 ID", required = true) @PathVariable final Integer userId) {
        return ResponseEntity.ok(folderService.getAllFolder(userId));
    }

    @ApiOperation("해당 ID의 폴더 정보를 가져옵니다.")
    @GetMapping(value = "/{folderId}}")
    public ResponseEntity<FolderResponseDto.Info> getFolderById(@ApiParam(value="폴더 ID", required = true) @PathVariable final Integer folderId) {
        return ResponseEntity.ok(folderService.getFolderById(folderId));
    }

    @ApiOperation("폴더를 이름으로 검색합니다.")
    @GetMapping(value="/search")
    public ResponseEntity<List<String>> searchFolder(@ApiParam(value="폴더 이름", required = true) @RequestParam String folderName) {
        return ResponseEntity.ok(folderService.searchFolder(folderName));
    }

    @ApiOperation("해당 폴더에 속한 모든 파일을 불러옵니다.")
    @GetMapping(value="/fileList/{folderId}")
    public ResponseEntity<List<FileResponseDto.~~## >> getAllFolderFile(@ApiParam(value="폴더 ID", required = true) @PathVariable final Integer folderId){
        return ResponseEntity.ok(folderService.getAllFolderFile(folderId));
    }

    @ApiOperation("해당 폴더를 favorite 값을 변경합니다.")
    @PostMapping(value="/favorite/{folderId}")
    public ResponseEntity<Boolean> favoirteFolder(@ApiParam(value="폴더 ID", required = true) @PathVariable final Integer folderId) {
        folderService.favoriteFolder(folderId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @ApiOperation("해당 폴더의 이름을 변경합니다.")
    @PostMapping(value="/rename/{folderId}")
    public ResponseEntity<String> renameFolder(@ApiParam(value="폴더 ID", required = true) @PathVariable final Integer folderId,
                                               @RequestParam String newName){
        return ResponseEntity.ok(folderService.renameFolder(folderId,newName));
    }

    @ApiOperation("해당 폴더를 휴지통에 넣습니다.")
    @PostMapping(value="/trash/{folderId}")
    public ResponseEntity<Integer> trashFolder(@ApiParam(value="폴더 ID", required = true) @PathVariable final Integer folderId) {
        return ResponseEntity.ok(folderService.trashFolder(folderId));
    }

    @ApiOperation("해당 폴더를 휴지통에서 복구합니다.")
    @PostMapping("value=/restore/{folderId}")
    public ResponseEntity<Integer> restoreFolder(@ApiParam(value="폴더 ID", required = true) @PathVariable final Integer folderId) {
        return ResponseEntity.ok(folderService.restoreFolder(folderId));
    }

    @ApiOperation("해당 폴더를 휴지통에서 완전히 삭제합니다.")
    @DeleteMapping("value=/delete/{fodlerId}")
    public ResponseEntity<Void> deleteFolder(@ApiParam(value="폴더 ID", required = true) @PathVariable final Integer folderId) {
        folderService.deleteFolder(folderId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @ApiOperation("휴지통에 있는 폴더를 보여줍니다.")
    @GetMapping("value=/trash/list/{userId}")
    public ResponseEntity<List<String>> getAllTrashFolder(@ApiParam(value="유저 ID", required = true) @PathVariable final Integer userId){
        return ResponseEntity.ok(folderService.getAllTrashFolder(userId));
    }
}
