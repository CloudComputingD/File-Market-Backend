package com.fs.filemarket.api.domain.folder.service;

import com.fs.filemarket.api.domain.folder.Folder;
import com.fs.filemarket.api.domain.folder.dto.FolderResponseDto;
import com.fs.filemarket.api.domain.folder.repository.FileFolderRepository;
import com.fs.filemarket.api.domain.folder.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FolderService {
    //아직 모든 api에서 유저 권한 확인은 설정 안 한 상태
    //=> 권한 검사, 중복 검사용 코드만 추가하면 됨
    //trash folder인지 filtering 해야함
    //s3관련 코드 아직 아무것도 안 짬!
    protected final FolderRepository folderRepository;
    protected final FileFolderRepository fileFolderRepository;
    private final UserService userService;

    @Transactional
    public String postFolder(String name){
        User user= userService.getCurrentUser();

        Folder folder = folderRepository.save(
                Folder.builder()
                        .name(name)
                        .created_time(LocalDateTime.now())
                        .user(user)
                        .build()

        );

        Integer id = folder.getId();
        return id.toString();
    }

    @Transactional(readOnly = true)
    public List<String> getAllFolder(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "해당하는 유저가 존재하지 않습니다."
        ));
        return folderRepository.findByUser(user).stream().map(Folder::getName).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FolderResponseDto.Info getFolderById(Integer folderId){
        return FolderResponseDto.Info.of(folderRepository.findById(folderId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "해당하는 ID를 가진 앨범이 존재하지 않습니다."
        )));
    }

    @Transactional(readOnly = true)
    public List<String> searchFolder(String name){
        return folderRepository.findByName(name).stream().map(Folder::getName).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<##dto(file dto)> getAllFolderFile(Integer folderId) {
        Folder folder = folderRepository.findById(folderId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "해당하는 ID를 가진 폴더가 존재하지 않습니다."
        ));
        return ##dto.file.of(fileFolderRepository.findByFolder(folder));
    }

    @Transactional
    public void favoriteFolder(Integer folderId) {
        Folder folder = folderRepository.findById(folderId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "해당하는 ID를 가진 폴더가 존재하지 않습니다."
        ));
        if(folder.isFavorite()){
            folder.setFavorite(false);
        }
        else {
            folder.setFavorite(true);
        }

        folderRepository.save(folder);
    }

    @Transactional
    public String renameFolder(Integer folderId, String newName) {
        Folder folder = folderRepository.findById(folderId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "해당하는 ID를 가진 폴더가 존재하지 않습니다."
        ));
        folder.setName(newName);
        folderRepository.save(folder);

        return newName;
    }

    @Transactional
    public Integer trashFolder(Integer folderId) {
        Folder folder = folderRepository.findById(folderId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "해당하는 ID를 가진 폴더가 존재하지 않습니다."
        ));
        //folder도 trash mark를 만들지, 아니면 folder 내 파일의 trash boolean을 true로 할지 정해야 함(이러면 복구에서 문제 발생)
        folder.setTrash(true);
        folder.setDeleted_time(LocalDateTime.now());
        //folder를 지원다고 폴더 내 파일들의 원친이 같이 삭제 되는게 아님
        //folder를 지우면.. filefolder에서 삭제 처리해야하는데 ..휴지통 가는거까지는 고려하지 말고~ 완전 삭제할 때만 cascade로 어차피 지워짐
        folderRepository.save(folder);

        return folderId;
    }

    @Transactional
    public Integer restoreFolder(Integer folderId) {
        Folder folder = folderRepository.findById(folderId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "해당하는 ID를 가진 폴더가 존재하지 않습니다."
        ));
        folder.setTrash(false);
        folder.setDeleted_time(null);
        folderRepository.save(folder);

        return folderId;
    }

    @Transactional
    public void deleteFolder(Integer folderId) {
        Folder folder = folderRepository.findById(folderId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "해당하는 ID를 가진 폴더가 존재하지 않습니다."
        ));
        folderRepository.delete(folder);
    }

    @Transactional(readOnly = true)
    public List<String> getAllTrashFolder(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "해당하는 유저가 존재하지 않습니다."
        ));
        return folderRepository.findByUserAndTrash(user).stream().map(Folder::getName).collect(Collectors.toList());
    }
}
