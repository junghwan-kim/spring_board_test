package com.mysite.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.board.entity.BoardFileEntity;

public interface BoardFileRepository extends JpaRepository<BoardFileEntity, Long> {

}
