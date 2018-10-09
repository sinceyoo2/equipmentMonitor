package com.syo.platform.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;

import com.syo.platform.entity.Article;
import com.syo.platform.entity.ArticleAttachment;

public interface ArticleService {

	void saveArticle(Article article);
	
	void saveArticle(Article article, List<ArticleAttachment> atths);
	
	Page<Article> findArticle(Date start, Date end, int pageNo, int pageSize, String vague);
	
	Page<Article> findArticleByAccount(String account, Date start, Date end, int pageNo, int pageSize, String vague);
	
	Article findArticleById(String id);
	
	void deleteArticle(String id, String contextPath);
	
	ArticleAttachment findAttchById(String id);
	
	void deleteAttachment(String id);
	 
}
