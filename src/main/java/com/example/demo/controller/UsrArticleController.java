package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dto.Article;
import com.example.demo.service.ArticleService;

@Controller
public class UsrArticleController {
	
	private ArticleService articleService;
	
	public UsrArticleController(ArticleService articleService) {
		this.articleService = articleService;
	}
	
	@GetMapping("/usr/article/write")
	@ResponseBody
	public String write(String title, String content) {
		
		this.articleService.writeArticle(title, content);
		
		return String.format("게시물이 생성되었습니다.");
	}
	
	@GetMapping("/usr/article/list")
	@ResponseBody
	public Object list() {
		
		List<Article> articles = this.articleService.getArticles();
		
		if (articles.size() == 0) {
			return "게시물이 존재하지 않습니다";
		}
		
		return articles;
	}
	
	@GetMapping("/usr/article/detail")
	@ResponseBody
	public Object detail(int id) {
		
		Article article = this.articleService.getArticleById(id);
		
		if (article == null) {
			return String.format("%d번 게시물은 존재하지 않습니다", id);
		}
		
		return article;
	}
	
	@GetMapping("/usr/article/modify")
	@ResponseBody
	public String modify(int id, String title, String content) {
		
//		Article article = this.articleService.getArticleById(id);
//		
//		if (article == null) {
//			return String.format("%d번 게시물은 존재하지 않습니다", id);
//		}
		
		this.articleService.modifyArticle(id, title, content);
		
		return String.format("%d번 게시물을 수정했습니다", id);
	}
	
	@GetMapping("/usr/article/delete")
	@ResponseBody
	public String delete(int id) {
		
//		Article article = this.articleService.getArticleById(id);
//		
//		if (article == null) {
//			return String.format("%d번 게시물은 존재하지 않습니다", id);
//		}
		
		this.articleService.deleteArticle(id);
		
		return String.format("%d번 게시물을 삭제했습니다", id);
	}
}