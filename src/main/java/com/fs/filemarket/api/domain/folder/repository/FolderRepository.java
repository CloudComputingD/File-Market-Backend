package com.fs.filemarket.api.domain.folder.repository;

import com.fs.filemarket.api.domain.folder.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderRepository extends JpaRepository<Folder, Integer> {
}
