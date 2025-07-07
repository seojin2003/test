package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dto.Article;
import com.example.demo.dto.Board;
import com.example.demo.dto.LoginedMember;
import com.example.demo.service.ArticleService;
import com.example.demo.service.BoardService;
import com.example.demo.util.Util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class UsrArticleController {

	private ArticleService articleService;
	private BoardService boardService;

	public UsrArticleController(ArticleService articleService, BoardService boardService) {
		this.articleService = articleService;
		this.boardService = boardService;
	}

	@GetMapping("/usr/article/write")
	public String write(Model model, @RequestParam(defaultValue = "2") int boardId) {
		model.addAttribute("boardId", boardId);
		return "usr/article/write";
	}

	@PostMapping("/usr/article/doWrite")
	@ResponseBody
	public String doWrite(String title, String content, int boardId, HttpServletRequest request) {

		HttpSession session = request.getSession();
		LoginedMember loginedMember = (LoginedMember) session.getAttribute("loginedMember");
		this.articleService.writeArticle(title, content, loginedMember.getId(), boardId);

		int id = this.articleService.getLastArticleId();

		return Util.jsReplace("게시글 작성!", String.format("detail?id=%d", id));
	}

	@GetMapping("/usr/article/list")
	public String list(Model model, int boardId, @RequestParam(defaultValue = "1") int cPage,
			@RequestParam(defaultValue = "title") String searchType,
			@RequestParam(defaultValue = "") String searchKeyword) {

		int articlesInPage = 10;
		int limitFrom = (cPage - 1) * articlesInPage;

		int articlesCnt = this.articleService.getArticlesCnt(boardId, searchType, searchKeyword);

		int totalPagesCnt = (int) Math.ceil(articlesCnt / (double) articlesInPage);

		int begin = ((cPage - 1) / 10) * 10 + 1;
		int end = (((cPage - 1) / 10) + 1) * 10;

		if (end > totalPagesCnt) {
			end = totalPagesCnt;
		}

		Board board = this.boardService.getBoard(boardId);
		List<Article> articles = this.articleService.getArticles(boardId, articlesInPage, limitFrom, searchType,
				searchKeyword);

		model.addAttribute("searchType", searchType);
		model.addAttribute("searchKeyword", searchKeyword);
		model.addAttribute("cPage", cPage);
		model.addAttribute("begin", begin);
		model.addAttribute("end", end);
		model.addAttribute("totalPagesCnt", totalPagesCnt);
		model.addAttribute("articlesCnt", articlesCnt);
		model.addAttribute("articles", articles);
		model.addAttribute("board", board);

		return "usr/article/list";
	}

	@GetMapping("/usr/article/detail")
	public String detail(Model model, int id) {
		
		Article article = this.articleService.getArticleById(id);
		
		model.addAttribute("article", article);

		return "usr/article/detail";
	}

	@GetMapping("/usr/article/modify")
	public String modify(Model model, int id) {

		Article article = this.articleService.getArticleById(id);

		model.addAttribute("article", article);

		return "usr/article/modify";
	}

	@PostMapping("/usr/article/doModify")
	@ResponseBody
	public String doModify(int id, String title, String content) {

		this.articleService.modifyArticle(id, title, content);

		return Util.jsReplace(String.format("%d번 게시물을 수정했습니다", id), String.format("detail?id=%d", id));
	}

	@GetMapping("/usr/article/delete")
	@ResponseBody
	public String delete(int id, int boardId) {

		this.articleService.deleteArticle(id);

		return Util.jsReplace(String.format("%d번 게시글이 삭제되었습니다", id), String.format("list?boardId=%d", boardId));
	}
}