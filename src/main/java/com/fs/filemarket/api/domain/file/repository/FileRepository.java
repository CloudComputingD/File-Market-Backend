package com.fs.filemarket.api.domain.file.repository;

import com.fs.filemarket.api.domain.file.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
public interface FileRepository extends JpaRepository<File, Integer> {
    @Query("select f.name from Folder f where f.user = :user and f.trash=false")
    List<File> findByUser(User user);
    @Query("select f.name from Folder f where f.trash=false")
    List<File> findByName(String name);
    @Query("select f.name from Folder f where f.user = :user and f.trash=true")
    List<File> findByUserAndTrash(User user);
}
