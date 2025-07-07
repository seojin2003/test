package com.example.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.demo.dto.Article;

@Mapper
public interface ArticleDao {
	
	@Insert("""
			INSERT INTO article
			    SET regDate = NOW()
			        , updateDate = NOW()
			        , memberId = #{loginedMemberId}
			        , boardId = #{boardId}
			        , title = #{title}
			        , content = #{content}
			""")
	public void writeArticle(String title, String content, int loginedMemberId, int boardId);

	@Select("""
			<script>
			SELECT a.*
					, m.loginId AS writerName
			    FROM article a
			    INNER JOIN `member` m
			    ON a.memberId = m.id
			    WHERE a.boardId = #{boardId}
			    <if test="searchKeyword != ''">
			    	<choose>
			    		<when test="searchType == 'title'">
				    		AND a.title LIKE CONCAT('%', #{searchKeyword}, '%')
			    		</when>
					    <when test="searchType == 'content'">
					    	AND a.content LIKE CONCAT('%', #{searchKeyword}, '%')
					    </when>
					    <otherwise>
					    	AND (
					    		a.title LIKE CONCAT('%', #{searchKeyword}, '%')
					    		OR a.content LIKE CONCAT('%', #{searchKeyword}, '%')
					    	)
					    </otherwise>
			    	</choose>
			    </if>
				ORDER BY a.id DESC
				LIMIT #{limitFrom}, #{articlesInPage}
			</script>
			""")
	public List<Article> getArticles(int boardId, int articlesInPage, int limitFrom, String searchType, String searchKeyword);
	
	@Select("""
			SELECT a.*, m.loginId AS writerName
			    FROM article a
			    INNER JOIN `member` m
			    ON a.memberId = m.id
				WHERE a.id = #{id}
			""")
	public Article getArticleById(int id);

	@Update("""
			UPDATE article
			    SET updateDate = NOW()
			        , title = #{title}
			        , content = #{content}
			    WHERE id = #{id}
			""")
	public void modifyArticle(int id, String title, String content);

	@Delete("""
			DELETE FROM article
				WHERE id = #{id}
			""")
	public void deleteArticle(int id);

	@Select("""
			SELECT LAST_INSERT_ID()
			""")
	public int getLastArticleId();

	@Select("""
			<script>
			SELECT COUNT(id)
				FROM article
				WHERE boardId = #{boardId}
				<if test="searchKeyword != ''">
			    	<choose>
			    		<when test="searchType == 'title'">
				    		AND title LIKE CONCAT('%', #{searchKeyword}, '%')
			    		</when>
					    <when test="searchType == 'content'">
					    	AND content LIKE CONCAT('%', #{searchKeyword}, '%')
					    </when>
					    <otherwise>
					    	AND (
					    		title LIKE CONCAT('%', #{searchKeyword}, '%')
					    		OR content LIKE CONCAT('%', #{searchKeyword}, '%')
					    	)
					    </otherwise>
			    	</choose>
			    </if>
			</script>
			""")
	public int getArticlesCnt(int boardId, String searchType, String searchKeyword);


}