package com.fs.filemarket.api.domain.folder.dto;

import com.fs.filemarket.api.domain.file.dto.FileResponseDto;
import com.fs.filemarket.api.domain.folder.FileFolder;
import com.fs.filemarket.api.domain.folder.Folder;
import com.fs.filemarket.api.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Schema(description = "folder의 responsedto")
public class FolderResponseDto {
    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    static public class Info {
        private Integer id;
        private String name;
        private LocalDateTime created_time;
        private LocalDateTime modified_time;
        private LocalDateTime deleted_time;
        private boolean favorite;
        private User user;
        private boolean trash;
        private Set<FileResponseDto.Info> files;

        public static Info of(Folder folder) {
            Set<FileResponseDto.Info> fileInfos = folder.getFiles()
                    .stream()
                    .map(FileFolder::getFile)
                    .map(FileResponseDto.Info::of) // FileFolder에서 File로 매핑 후 FileResponseDto.Info 생성
                    .collect(Collectors.toSet());
            InfoBuilder builder = Info.builder()
                    .id(folder.getId())
                    .name(folder.getName())
                    .created_time(folder.getCreated_time())
                    .modified_time(folder.getModified_time())
                    .deleted_time(folder.getDeleted_time())
                    .favorite(folder.isFavorite())
                    .trash(folder.isTrash())
                    .files(fileInfos)
                    .user(folder.getUser());

            return builder.build();
        }
    }
}
