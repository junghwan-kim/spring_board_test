package com.mysite.board.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.board.entity.BoardEntity;
import com.mysite.board.entity.CommentEntity;

public interface CommentRepository extends JpaRepository<CommentEntity, Long>{
	List<CommentEntity> findAllByBoardEntityOrderByIdDesc(BoardEntity boardEntity);
}
