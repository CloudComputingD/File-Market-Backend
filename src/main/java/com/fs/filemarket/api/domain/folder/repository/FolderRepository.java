package com.fs.filemarket.api.domain.folder.repository;

import com.fs.filemarket.api.domain.folder.Folder;
import com.fs.filemarket.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Integer> {
    @Query("select f.name from Folder f where f.user = :user and f.trash=false")
    List<Folder> findByUser(User user);
    @Query("select f.name from Folder f where f.trash=false")
    List<Folder> findByName(String name);
    @Query("select f.name from Folder f where f.user = :user and f.trash=true")
    List<Folder> findByUserAndTrash(User user);
}
