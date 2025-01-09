package com.mysite.board.dto;

import java.time.LocalDateTime;

import com.mysite.board.entity.CommentEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentDto {

	private Long id;
	private String commentWriter;
	private String commentContents;
	private Long boardId;
	private LocalDateTime commentCreatedTime;
	public static CommentDto toCommentDto(CommentEntity commentEntity, Long boardId) {
		
		CommentDto commentDto = new CommentDto();
		commentDto.setId(commentEntity.getId());
		commentDto.setCommentWriter(commentEntity.getCommentWriter());
		commentDto.setCommentContents(commentEntity.getCommentContents());
		commentDto.setCommentCreatedTime(commentEntity.getCreatedTime());		
		commentDto.setBoardId(boardId);
		return commentDto;
	}
}
