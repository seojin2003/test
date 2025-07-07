package com.example.demo.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.demo.dto.Member;

@Mapper
public interface MemberDao {

	@Insert("""
			INSERT INTO `member`
			    SET regDate = NOW()
			        , updateDate = NOW()
			        , loginId = #{loginId}
			        , loginPw = #{loginPw}
			        , `name` = #{name}
			""")
	void joinMember(String loginId, String loginPw, String name);

	@Select("""
			SELECT *
				FROM `member`
				WHERE loginId = #{loginId}
			""")
	Member getMemberByLoginId(String loginId);

	@Select("""
			SELECT loginId
				FROM `member`
				WHERE id = #{id}
			""")
	String getLoginId(int id);
	
	@Select("""
			SELECT *
				FROM `member`
				WHERE `name` = #{name}
			""")
	Member getMemberByNameAndEmail(String name);

	@Update("""
			UPDATE `member`
				SET updateDate = NOW()
					, loginPw = #{loginPw}
				WHERE id = #{id}
			""")
	void modifyPassword(int id, String loginPw);
	
	@Select("""
			SELECT *
				FROM `member`
				WHERE id = #{loginedMemberId}
			""")
	Member getMemberById(int id);

	@Update("""
			UPDATE `member`
				SET updateDate = NOW()
					, name = #{name}
				WHERE id = #{id}
			""")
	void modifyMember(int id, String name);
}