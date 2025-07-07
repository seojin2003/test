package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dto.LoginedMember;
import com.example.demo.dto.Member;
import com.example.demo.service.MemberService;
import com.example.demo.util.Util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class UsrMemberController {
	
	private MemberService memberService;
	
	public UsrMemberController(MemberService memberService) {
		this.memberService = memberService;
	}
	
	@GetMapping("/usr/member/join")
	public String join() {
		return "usr/member/join";
	}
	
	@PostMapping("/usr/member/doJoin")
	@ResponseBody
	public String doJoin(String loginId, String loginPw, String name, String email) {
		
		Member existingMember = this.memberService.getMemberByLoginId(loginId);
		if (existingMember != null) {
			return Util.jsReplace(String.format("[ %s ] 은(는) 이미 사용중인 아이디입니다", loginId), "join");
		}
		
		this.memberService.joinMember(loginId, Util.encryptSHA256(loginPw), name, email);
		
		return Util.jsReplace(String.format("[ %s ] 님의 가입이 완료되었습니다", name), "/");
	}
	
	@PostMapping("/usr/member/checkLoginIdDup")
	@ResponseBody
	public String checkLoginIdDup(String loginId) {
		
		if (loginId == null || loginId.trim().length() == 0) {
			return "아이디를 입력해주세요";
		}
		
		Member existingMember = this.memberService.getMemberByLoginId(loginId.trim());
		if (existingMember != null) {
			return "이미 사용중인 아이디입니다";
		}
		
		return "사용 가능한 아이디입니다";
	}
	

	
	@GetMapping("/usr/member/login")
	public String login() {
		return "usr/member/login";
	}
	
	@PostMapping("/usr/member/doLogin")
	@ResponseBody
	public String doLogin(String loginId, String loginPw, HttpServletRequest request) {
		
		Member member = this.memberService.getMemberByLoginId(loginId);
		
		if (member == null) {
			return Util.jsReplace(String.format("[ %s ] 은(는) 존재하지 않는 회원입니다", loginId), "login");
		}
		
		if (member.getLoginPw().equals(Util.encryptSHA256(loginPw)) == false) {
			return Util.jsReplace("비밀번호가 일치하지 않습니다", "login");
		}
		
		HttpSession session = request.getSession();
		session.setAttribute("loginedMember", new LoginedMember(member.getId(), member.getAuthLevel()));
		
		return Util.jsReplace(String.format("[ %s ] 님 환영합니다", member.getLoginId()), "/");
	}
	
	@GetMapping("/usr/member/logout")
	public String logout(HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		session.removeAttribute("loginedMember");
		
		return "redirect:/usr/member/login";
	}
	
	@GetMapping("/usr/member/myPage")
	public String myPage(Model model, HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		LoginedMember loginedMember = (LoginedMember) session.getAttribute("loginedMember");
		Member member = this.memberService.getMemberById(loginedMember.getId());
		
		model.addAttribute("member", member);
		
		return "usr/member/myPage";
	}

	@GetMapping("/usr/member/modify")
	public String modify(int id, String name, String loginPw) {
		this.memberService.modifyMember(id, name, null);
		if (loginPw != null && loginPw.trim().length() > 0) {
			this.memberService.modifyPassword(id, Util.encryptSHA256(loginPw));
		}
		return "redirect:/usr/member/myPage";
	}
}