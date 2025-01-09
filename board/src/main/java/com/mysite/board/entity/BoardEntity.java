package com.mysite.board.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.domain.EntityScan;

import com.mysite.board.dto.BoardDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name ="board_table")
public class BoardEntity extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 20, nullable = false)
	private String boardWriter;

	
	@Column
	private String boardPass;
	
	@Column
	private String boardTitle;
	
	@Column(length = 500)
	private String boardContents;
	
	@Column
	private int boardHits;
	
	@Column
	private int fileAttached; // 1 or 0
	
	
	@OneToMany(mappedBy = "boardEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<BoardFileEntity> boardFileEntityList = new ArrayList<>();
	
	@OneToMany(mappedBy = "boardEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<CommentEntity> commentEntityList = new ArrayList<>();
	
	
	public static BoardEntity toSaveEntity(BoardDto boardDto) {
		BoardEntity boardEntity = new BoardEntity();
		boardEntity.setBoardWriter(boardDto.getBoardWriter());
		boardEntity.setBoardPass(boardDto.getBoardPass());
		boardEntity.setBoardTitle(boardDto.getBoardTitle());
		boardEntity.setBoardContents(boardDto.getBoardContents());
		boardEntity.setBoardHits(0);
		boardEntity.setFileAttached(0); //파일없음
		return boardEntity;
	}


	public static BoardEntity toUpdateEntity(BoardDto boardDto) {
		// TODO Auto-generated method stub
		BoardEntity boardEntity = new BoardEntity();
		boardEntity.setId(boardDto.getId());
		boardEntity.setBoardWriter(boardDto.getBoardWriter());
		boardEntity.setBoardPass(boardDto.getBoardPass());
		boardEntity.setBoardTitle(boardDto.getBoardTitle());
		boardEntity.setBoardContents(boardDto.getBoardContents());
		boardEntity.setBoardHits(boardDto.getBoardHits());
		return boardEntity;
	}


	public static BoardEntity toSaveFileEntity(BoardDto boardDto) {
		BoardEntity boardEntity = new BoardEntity();
		boardEntity.setBoardWriter(boardDto.getBoardWriter());
		boardEntity.setBoardPass(boardDto.getBoardPass());
		boardEntity.setBoardTitle(boardDto.getBoardTitle());
		boardEntity.setBoardContents(boardDto.getBoardContents());
		boardEntity.setBoardHits(0);
		boardEntity.setFileAttached(1); //파일있음
		return boardEntity;	
	}
	
}
