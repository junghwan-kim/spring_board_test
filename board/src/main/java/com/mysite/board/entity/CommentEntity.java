package com.mysite.board.entity;

import com.mysite.board.dto.CommentDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "comment_table")
public class CommentEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	@Column(length = 20, nullable = false)
	private String commentWriter;
	
	@Column
	private String commentContents;
	
	//BOARD를 참조 
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "board_id")
	private BoardEntity boardEntity;

	public static CommentEntity toSaveEntity(CommentDto commentDto, BoardEntity boardEntity) {
		CommentEntity commentEntity = new CommentEntity();
		commentEntity.setCommentWriter(commentDto.getCommentWriter());
		commentEntity.setCommentContents(commentDto.getCommentContents());
		commentEntity.setBoardEntity(boardEntity);		
		return commentEntity;
	}

}
