package com.mysite.board.controller;


import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mysite.board.dto.BoardDto;
import com.mysite.board.dto.CommentDto;
import com.mysite.board.service.BoardService;
import com.mysite.board.service.CommentService;

import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
	
	private final BoardService boardService;
	private final CommentService commentService;

	@GetMapping("/save")
	public String saveForm() {
		return "save";
	}
	
	@PostMapping("/save")
	public String save(@ModelAttribute BoardDto boardDto) throws IOException {
		this.boardService.save(boardDto);
		return "redirect:/board/paging";
	}
	
	@GetMapping("/list")
	public String findAll(Model model) {
		List<BoardDto> boardDtoList = this.boardService.findAll();
		System.out.println(boardDtoList);
		model.addAttribute("boardList",boardDtoList);
		return "list";
		
	}
	
	@GetMapping("/detail/{id}")
	public String findById(@PathVariable("id") Long id, Model model, @PageableDefault(page=1) Pageable pageable) {
		this.boardService.updateHits(id);
		BoardDto boardDto = this.boardService.findById(id);
		
		//댓글 목록 가져오기
		
		List<CommentDto> commentDtoList = this.commentService.findAll(id);		
		model.addAttribute("commentList", commentDtoList);
		model.addAttribute("board",boardDto);
		model.addAttribute("page", pageable.getPageNumber());
		return "detail";
	}
	
	@GetMapping("/update/{id}")
	public String updateForm(@PathVariable("id") Long id, Model model) {
		BoardDto boardDto = this.boardService.findById(id);
		model.addAttribute("boardUpdate",boardDto);
		return "update";
	}
	
	@PostMapping("/update")
	public String update(@ModelAttribute BoardDto boardDto, Model model) {
		BoardDto board =  this.boardService.update(boardDto);
		model.addAttribute("board",board);
		return "detail";
		//return "redirect:/board/detail/"+boardDto.getId();
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") Long id) {
		this.boardService.delete(id);
		return "redirect:/board/list";
	}
	
	@GetMapping("/paging")
	public String paging(@PageableDefault(page=1) Pageable pageable, Model model) {
		Page<BoardDto> boardList = boardService.paging(pageable);
		
		int blockLimit = 3;
		int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; // 1 4 7 10 ~~
        int endPage = ((startPage + blockLimit - 1) < boardList.getTotalPages()) ? startPage + blockLimit - 1 : boardList.getTotalPages();
        
        // page 갯수 20개
        // 현재 사용자가 3페이지
        // 1 2 3
        // 현재 사용자가 7페이지
        // 7 8 9
        // 보여지는 페이지 갯수 3개
        // 총 페이지 갯수 8개
        
        System.out.println(boardList);
        
		model.addAttribute("boardList",boardList);
		model.addAttribute("startPage",startPage);
		model.addAttribute("endPage",endPage);
		
		return "paging";
	}
}
