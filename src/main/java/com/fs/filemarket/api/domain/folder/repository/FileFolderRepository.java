package com.fs.filemarket.api.domain.folder.repository;

import com.fs.filemarket.api.domain.file.File;
import com.fs.filemarket.api.domain.folder.FileFolder;
import com.fs.filemarket.api.domain.folder.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileFolderRepository extends JpaRepository<FileFolder, Integer> {
    @Query("select f.file from FileFolder f where f.folder = :folder and f.file.trash = false")
    List<File> findAllByFolder(Folder folder);
}
