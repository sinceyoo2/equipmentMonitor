package com.syo.platform.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.syo.platform.entity.Article;

public interface ArticleRepository extends JpaRepository<Article, String> {

	@Query("from Article a where a.tag like %:vague% or a.title like %:vague% or a.summary like %:vague% or a.vagueTags like %:vague%")
	public Page<Article> findAll(Pageable page,  @Param("vague")String vague);
	
	@Query("from Article a where a.createTime>=:start and a.createTime<:end")
	public Page<Article> findAll(Pageable page, @Param("start")Date start, @Param("end")Date end);
	
	@Query("from Article a where a.createTime>=:start and a.createTime<:end and (a.tag like %:vague% or a.title like %:vague% or a.summary like %:vague% or a.vagueTags like %:vague%)")
	public Page<Article> findAll(Pageable page, @Param("start")Date start, @Param("end")Date end,  @Param("vague")String vague);
	
	@Query("from Article a where a.author.account=:account")
	public Page<Article> findByAccount(Pageable page, @Param("account")String account);
	
	@Query("from Article a where a.author.account=:account and (a.tag like %:vague% or a.title like %:vague% or a.summary like %:vague% or a.vagueTags like %:vague%)")
	public Page<Article> findByAccount(Pageable page, @Param("account")String account, @Param("vague")String vague);
	
	@Query("from Article a where a.author.account=:account and a.createTime>=:start and a.createTime<:end")
	public Page<Article> findByAccount(Pageable page,  @Param("account")String account,  @Param("start")Date start,  @Param("end")Date end);
	
	@Query("from Article a where a.author.account=:account and a.createTime>=:start and a.createTime<:end and (a.tag like %:vague% or a.title like %:vague% or a.summary like %:vague% or a.vagueTags like %:vague%)")
	public Page<Article> findByAccount(Pageable page,  @Param("account")String account, @Param("start")Date start, @Param("end")Date end,  @Param("vague")String vague);
	
}
