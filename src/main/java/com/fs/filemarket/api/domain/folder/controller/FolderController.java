package com.fs.filemarket.api.domain.folder.controller;

import com.fs.filemarket.api.domain.file.dto.FileResponseDto;
import com.fs.filemarket.api.domain.folder.dto.FolderResponseDto;
import com.fs.filemarket.api.domain.folder.service.FolderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Folder", description = "Folder Controller")
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

    @Operation(summary = "폴더를 생성합니다.")
    @PostMapping(value = "")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<String> postFolder(@Parameter(description = "폴더 정보")
                                           @Valid @RequestParam String name) {
        return ResponseEntity.status(HttpStatus.CREATED).body("/folder/" + folderService.postFolder(name));
    }

    @Operation(summary ="유저의 전체 폴더 list를 반환합니다.")
    @GetMapping(value = "/list/{userId}")
    public ResponseEntity<List<String>> getAllFolder(@Parameter(description = "유저 ID", required = true) @PathVariable final Integer userId) {
        return ResponseEntity.ok(folderService.getAllFolder(userId));
    }

    @Operation(summary ="해당 ID의 폴더 정보를 가져옵니다.")
    @GetMapping(value = "/{folderId}")
    public ResponseEntity<FolderResponseDto.Info> getFolderById(@Parameter(description ="폴더 ID", required = true) @PathVariable final Integer folderId) {
        return ResponseEntity.ok(folderService.getFolderById(folderId));
    }

    @Operation(summary ="폴더를 이름으로 검색합니다.")
    @GetMapping(value="/search")
    public ResponseEntity<List<String>> searchFolder(@Parameter(description ="폴더 이름", required = true) @RequestParam String folderName) {
        return ResponseEntity.ok(folderService.searchFolder(folderName));
    }

    @Operation(summary ="해당 폴더에 속한 모든 파일을 불러옵니다.")
    @GetMapping(value="/fileList/{folderId}")
    public ResponseEntity<List<FileResponseDto.Info>> getAllFolderFile(@Parameter(description ="폴더 ID", required = true) @PathVariable final Integer folderId){
        return ResponseEntity.ok(folderService.getAllFolderFile(folderId));
    }

    @Operation(summary ="해당 폴더를 favorite 값을 변경합니다.")
    @PostMapping(value="/favorite/{folderId}")
    public ResponseEntity<Boolean> favoirteFolder(@Parameter(description ="폴더 ID", required = true) @PathVariable final Integer folderId) {
        folderService.favoriteFolder(folderId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @Operation(summary ="해당 폴더의 이름을 변경합니다.")
    @PostMapping(value="/rename/{folderId}")
    public ResponseEntity<String> renameFolder(@Parameter(description ="폴더 ID", required = true) @PathVariable final Integer folderId,
                                               @RequestParam String newName){
        return ResponseEntity.ok(folderService.renameFolder(folderId,newName));
    }

    @Operation(summary ="해당 폴더를 휴지통에 넣습니다.")
    @PostMapping(value="/trash/{folderId}")
    public ResponseEntity<Integer> trashFolder(@Parameter(description ="폴더 ID", required = true) @PathVariable final Integer folderId) {
        return ResponseEntity.ok(folderService.trashFolder(folderId));
    }

    @Operation(summary ="해당 폴더를 휴지통에서 복구합니다.")
    @PostMapping("value=/restore/{folderId}")
    public ResponseEntity<Integer> restoreFolder(@Parameter(description ="폴더 ID", required = true) @PathVariable final Integer folderId) {
        return ResponseEntity.ok(folderService.restoreFolder(folderId));
    }

    @Operation(summary ="해당 폴더를 휴지통에서 완전히 삭제합니다.")
    @DeleteMapping("value=/delete/{fodlerId}")
    public ResponseEntity<Void> deleteFolder(@Parameter(description ="폴더 ID", required = true) @PathVariable final Integer folderId) {
        folderService.deleteFolder(folderId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @Operation(summary ="휴지통에 있는 폴더를 보여줍니다.")
    @GetMapping("value=/trash/list/{userId}")
    public ResponseEntity<List<String>> getAllTrashFolder(@Parameter(description ="유저 ID", required = true) @PathVariable final Integer userId){
        return ResponseEntity.ok(folderService.getAllTrashFolder(userId));
    }
}
