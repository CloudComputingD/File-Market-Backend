package com.fs.filemarket.api.domain.file.dto;

import com.fs.filemarket.api.domain.file.File;
import com.fs.filemarket.api.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
@Schema(description = "file의 responsedto")
public class FileResponseDto {
    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    static public class Info {
        private Integer id;
        private String name;
        private Integer file_size;
        private String extension;
        private LocalDateTime modified_time;
        private boolean favorite;
        private User user;

        public static FileResponseDto.Info of(File file) {
            return Info.builder()
                    .id(file.getId())
                    .name(file.getName())
                    .file_size(file.getFile_size())
                    .extension(file.getExtension())
                    .modified_time(file.getModified_time())
                    .favorite(file.isFavorite())
                    .user(file.getUser())
                    .build();
        }

//        public static List<File> of(List<FileFolder> files) {
//            return  files.stream().map(File::of).collect(Collectors.toList());
//        }
    }
}