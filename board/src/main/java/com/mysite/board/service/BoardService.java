package com.mysite.board.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mysite.board.dto.BoardDto;
import com.mysite.board.entity.BoardEntity;
import com.mysite.board.entity.BoardFileEntity;
import com.mysite.board.repository.BoardFileRepository;
import com.mysite.board.repository.BoardRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.asm.Advice.This;

// DTO -> Entity (Entity Class)
// Entity -> DTO (DTO Class)

@Service
@RequiredArgsConstructor
public class BoardService {
	private final BoardRepository boardRepository;
	private final BoardFileRepository boardFileRepository;
	
	public void save(BoardDto boardDto) throws IOException {
		//파일 첨부 여부에 따라 로직 분리
		if(boardDto.getBoardFile().isEmpty()) {
			//첨부파일이 없다
			BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDto);
			this.boardRepository.save(boardEntity);
		} else {
			//첨부파일이 있음
			/*
			 1. dto에 담긴 파일을 꺼냄
			 2. 파일의 이름을 가져옴
			 3. 서버 저장용 이름을 만듦
			 4. 저장 경로 설정
			 5. 해당 경로에 파일 저장
			 6. board_table에 해당 데이터 save처리
			 7. board_file_table에 해당 데이터 save처리
			 */
			
			BoardEntity boardEntity = BoardEntity.toSaveFileEntity(boardDto);
			Long savedId = this.boardRepository.save(boardEntity).getId();
			BoardEntity board = this.boardRepository.findById(savedId).get();
			for(MultipartFile boardFile: boardDto.getBoardFile()) {
				//MultipartFile boardFile = boardDto.getBoardFile(); // 1.
				String origianlFilename = boardFile.getOriginalFilename(); // 2.
				String storedFileName = System.currentTimeMillis()+"_"+origianlFilename; //3.
				String savePath = "D:/study/springboot_img/"+storedFileName; //4
				boardFile.transferTo(new File(savePath)); // 5
				
					
				
				BoardFileEntity boardFileEntity = BoardFileEntity.toBoardFileEntity(boardEntity, origianlFilename, storedFileName);
				this.boardFileRepository.save(boardFileEntity);
			}
			
			
		}
		
	}
	
	@Transactional
	public List<BoardDto> findAll(){
		List<BoardEntity> boardEntityList = this.boardRepository.findAll();
		List<BoardDto> boardDtoList = new ArrayList<>();
		for(BoardEntity boardEntity: boardEntityList) {
			boardDtoList.add(BoardDto.toBoardDto(boardEntity));			
		}
		
		return boardDtoList;
	}
	
	@Transactional
	public void updateHits(Long id) {
		this.boardRepository.updateHits(id);
	}
	
	@Transactional
	public BoardDto findById(Long id) {
		Optional<BoardEntity> optionalBoardEntity = this.boardRepository.findById(id);
		if(optionalBoardEntity.isPresent()) {
			BoardEntity boardEntity = optionalBoardEntity.get();
			BoardDto boardDto = BoardDto.toBoardDto(boardEntity);
			return boardDto;
		} else {
			return null;
		}
	}

	public BoardDto update(BoardDto boardDto) {
		this.boardRepository.save(BoardEntity.toUpdateEntity(boardDto));
		return findById(boardDto.getId());
	}

	public void delete(Long id) {
		// TODO Auto-generated method stub
		this.boardRepository.deleteById(id);
	}

	public Page<BoardDto> paging(Pageable pageable) {
		// TODO Auto-generated method stub
		int page = pageable.getPageNumber() - 1;
		int pageLimits = 3;
		Page<BoardEntity> boardEntities = this.boardRepository.findAll(PageRequest.of(page, pageLimits, Sort.by(Sort.Direction.DESC, "id")));
		
		System.out.println("boardEntities.getContent() = " + boardEntities.getContent()); // 요청 페이지에 해당하는 글
        System.out.println("boardEntities.getTotalElements() = " + boardEntities.getTotalElements()); // 전체 글갯수
        System.out.println("boardEntities.getNumber() = " + boardEntities.getNumber()); // DB로 요청한 페이지 번호
        System.out.println("boardEntities.getTotalPages() = " + boardEntities.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardEntities.getSize() = " + boardEntities.getSize()); // 한 페이지에 보여지는 글 갯수
        System.out.println("boardEntities.hasPrevious() = " + boardEntities.hasPrevious()); // 이전 페이지 존재 여부
        System.out.println("boardEntities.isFirst() = " + boardEntities.isFirst()); // 첫 페이지 여부
        System.out.println("boardEntities.isLast() = " + boardEntities.isLast()); // 마지막 페이지 여부
        
        
        Page<BoardDto> boardDtos = boardEntities.map(board -> new BoardDto(board.getId(), board.getBoardWriter(), board.getBoardTitle(), board.getBoardHits(), board.getCreatedTime()));
        
        return boardDtos;
	}
}
