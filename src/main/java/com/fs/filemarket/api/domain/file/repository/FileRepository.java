package com.fs.filemarket.api.domain.file.repository;

import com.fs.filemarket.api.domain.file.File;
import com.fs.filemarket.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
public interface FileRepository extends JpaRepository<File, Integer> {
    @Query("select f from File f where f.user = :user and f.trash=false")
    List<File> findByUser(User user);
    @Query("select f from File f where f.name = :name and f.trash=false")
    List<File> findByName(String name);
    @Query("select f from File f where f.user = :user and f.trash=true")
    List<File> findByUserAndTrash(User user);
    @Query("select SUM(f.file_size) FROM File f WHERE f.user = :user")
    Integer findTotalFileSizeByUser(User user);
}
