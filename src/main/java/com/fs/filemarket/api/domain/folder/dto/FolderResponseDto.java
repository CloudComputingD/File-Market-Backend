package com.fs.filemarket.api.domain.folder.dto;

import com.fs.filemarket.api.domain.file.dto.FileResponseDto;
import com.fs.filemarket.api.domain.folder.FileFolder;
import com.fs.filemarket.api.domain.folder.Folder;
import com.fs.filemarket.api.domain.user.User;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class FolderResponseDto {
    @ApiModel(value = "폴더 세부 정보")
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
        private Set<FileResponseDto.Info> files;

        public static Info of(Folder folder) {
            return Info.builder()
                    .id(folder.getId())
                    .name(folder.getName())
                    .created_time(folder.getCreated_time())
                    .modified_time(folder.getModified_time())
                    .deleted_time(folder.getDeleted_time())
                    .favorite(folder.isFavorite())
                    .user(folder.getUser())
                    .files(folder.getFiles()
                            .stream()
                            .map(FileFolder::getFile)
                            .map(FileResponseDto.Info::of)
                            .collect(Collectors.toSet()))
                    .build();
        }
    }
}
