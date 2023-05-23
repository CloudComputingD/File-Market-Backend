package com.fs.filemarket.api.domain.file.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fs.filemarket.api.domain.file.File;

import org.springframework.data.jpa.repository.JpaRepository;

//repository 가 db를 건드리는 부분
public interface FileRepository extends JpaRepository<File, Integer> {
}
