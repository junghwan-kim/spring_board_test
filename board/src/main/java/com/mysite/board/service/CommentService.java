package com.mysite.board.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mysite.board.dto.CommentDto;
import com.mysite.board.entity.BoardEntity;
import com.mysite.board.entity.CommentEntity;
import com.mysite.board.repository.BoardRepository;
import com.mysite.board.repository.CommentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
	private final CommentRepository commentRepository;
	private final BoardRepository boardRepository;

	public Long save(CommentDto commentDto) {
		
		//부모엔티티(BoardEntity) 조회
		Optional<BoardEntity> optionalBoardEntity = this.boardRepository.findById(commentDto.getBoardId());
		if(optionalBoardEntity.isPresent()) {
			BoardEntity boardEntity = optionalBoardEntity.get();
			CommentEntity commentEntity = CommentEntity.toSaveEntity(commentDto, boardEntity);
			return this.commentRepository.save(commentEntity).getId();
		} else {
			return null;
		}
		
	}

	public List<CommentDto> findAll(Long boardId) {
		
		BoardEntity boardEntity = this.boardRepository.findById(boardId).get();
		List<CommentEntity> commentEntityList = this.commentRepository.findAllByBoardEntityOrderByIdDesc(boardEntity);
		
		// Entity -> dto list
		
		List<CommentDto> commentDtoList = new ArrayList<>();
		for(CommentEntity commentEntity: commentEntityList) {
			CommentDto commentDto = CommentDto.toCommentDto(commentEntity, boardId);
			commentDtoList.add(commentDto);
		}
		return commentDtoList;
	}
}
