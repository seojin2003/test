package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.dao.MemberDao;
import com.example.demo.dto.Member;

@Service
public class MemberService {

	private MemberDao memberDao;

	public MemberService(MemberDao memberDao) {
		this.memberDao = memberDao;
	}

	public void joinMember(String loginId, String loginPw, String name, String email) {
		this.memberDao.joinMember(loginId, loginPw, name);
	}

	public Member getMemberByLoginId(String loginId) {
		return this.memberDao.getMemberByLoginId(loginId);
	}

	public Member getMemberById(int id) {
		return this.memberDao.getMemberById(id);
	}

	public void modifyMember(int id, String name, String email) {
		this.memberDao.modifyMember(id, name);
	}

	public void modifyPassword(int id, String loginPw) {
		this.memberDao.modifyPassword(id, loginPw);
	}
}