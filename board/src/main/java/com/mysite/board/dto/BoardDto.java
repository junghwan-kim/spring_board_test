package com.mysite.board.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.mysite.board.entity.BoardEntity;
import com.mysite.board.entity.BoardFileEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BoardDto {
	private Long id;
	private String boardWriter;
	private String boardPass;
	private String boardTitle;
	private String boardContents;
	private int boardHits;
	private LocalDateTime boardCreatedTime;
	private LocalDateTime boardUpdatedTime;
	
	private List<MultipartFile> boardFile;	//sava.html > controller 파일 담는 용도
	private List<String> originalFileName; //원본파일이름
	private List<String> storedFileName; //서버 저장용 파일 이름
	private int fileAttached; //파일 첨부 여부(첨부 1, 미첨부 0)
	
	
	public static BoardDto toBoardDto(BoardEntity boardEntity) {
		BoardDto boardDto = new BoardDto();
		boardDto.setId(boardEntity.getId());
		boardDto.setBoardWriter(boardEntity.getBoardWriter());
		boardDto.setBoardTitle(boardEntity.getBoardTitle());
		boardDto.setBoardPass(boardEntity.getBoardPass());
		boardDto.setBoardContents(boardEntity.getBoardContents());
		boardDto.setBoardHits(boardEntity.getBoardHits());
		boardDto.setBoardCreatedTime(boardEntity.getCreatedTime());
		boardDto.setBoardUpdatedTime(boardEntity.getUpdatedTime());
		if(boardEntity.getFileAttached() ==0) {
			boardDto.setFileAttached(boardEntity.getFileAttached());
		} else {
			
			List<String> originalFileNameList = new ArrayList<>();
			List<String> storedFileNameList = new ArrayList<>();
			
			boardDto.setFileAttached(boardEntity.getFileAttached());
			// 파일 이름을 가져가야함.
			
			for(BoardFileEntity boardFileEntity: boardEntity.getBoardFileEntityList()) {
				originalFileNameList.add(boardFileEntity.getOriginalFileName());
				storedFileNameList.add(boardFileEntity.getStoredFileName());
				//boardDto.setOriginalFileName(boardEntity.getBoardFileEntityList().get(0).getOriginalFileName());
				//boardDto.setStoredFileName(boardEntity.getBoardFileEntityList().get(0).getStoredFileName());
			}
			boardDto.setOriginalFileName(originalFileNameList);
			boardDto.setStoredFileName(storedFileNameList);
		}
		return boardDto;
	}


	public BoardDto(Long id, String boardWriter, String boardTitle, int boardHits, LocalDateTime boardCreatedTime) {
		this.id = id;
		this.boardWriter = boardWriter;
		this.boardTitle = boardTitle;
		this.boardHits = boardHits;
		this.boardCreatedTime = boardCreatedTime;
	}
}
