package com.syo.platform.service.impl;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.syo.platform.entity.Article;
import com.syo.platform.entity.ArticleAttachment;
import com.syo.platform.repository.ArticleAttachmentRepository;
import com.syo.platform.repository.ArticleRepository;
import com.syo.platform.service.ArticleService;

@Service
public class ArticleServiceImpl implements ArticleService {

	@Autowired
	private ArticleRepository articleRepository;
	
	@Autowired
	private ArticleAttachmentRepository articleAttachmentRepository;
	
	@Override
	@Transactional
	public void saveArticle(Article article) {
//		articleRepository.save(article);
//		if(article.getAttachments()!=null && article.getAttachments().size()>0) {
//			articleAttachmentRepository.save(article.getAttachments().get(0));
//		}
//		articleRepository.save(article);
//		System.out.println("zzzzzzzzzzzzzzzzzzzzzzz");
	}
	
	@Override
	@Transactional
	public void saveArticle(Article article, List<ArticleAttachment> atths) {
		article = articleRepository.save(article);
		if(atths!=null && atths.size()>0) {
			for(ArticleAttachment atth : atths) {
				atth.setArticle(article);
				articleAttachmentRepository.save(atth);
			}
		}
	}
	
	@Override
	public Page<Article> findArticleByAccount(String account, Date start, Date end, int pageNo, int pageSize,
			String vague) {
		PageRequest pageRequest = new PageRequest(pageNo-1, pageSize, Sort.Direction.DESC, "createTime");
		
		boolean isVagueNull = vague==null || vague.trim().equals("");
		boolean isDateNull = start==null || end==null;
		
		if(isVagueNull && isDateNull) {
			return articleRepository.findByAccount(pageRequest, account);
		}
		
		if(isVagueNull && !isDateNull) {
			return articleRepository.findByAccount(pageRequest, account, start, end);
		}
		
		if(!isVagueNull && isDateNull) {
			return articleRepository.findByAccount(pageRequest, account, vague);
		}
		
		if(!isVagueNull && isDateNull) {
			return articleRepository.findByAccount(pageRequest, account, start, end, vague);
		}
		
		return null;
	}

	@Override
	public Page<Article> findArticle(Date start, Date end, int pageNo, int pageSize, String vague) {
		PageRequest pageRequest = new PageRequest(pageNo-1, pageSize, Sort.Direction.DESC, "createTime");
		
		boolean isVagueNull = vague==null || vague.trim().equals("");
		boolean isDateNull = start==null || end==null;
		
		if(isVagueNull && isDateNull) {
			return articleRepository.findAll(pageRequest);
		}
		
		if(isVagueNull && !isDateNull) {
			return articleRepository.findAll(pageRequest, start, end);
		}
		
		if(!isVagueNull && isDateNull) {
			return articleRepository.findAll(pageRequest, vague);
		}
		
		if(!isVagueNull && isDateNull) {
			return articleRepository.findAll(pageRequest, start, end, vague);
		}
		
		return null;
	}

	@Override
	public Article findArticleById(String id) {
		return articleRepository.findOne(id);
	}

	@Override
	@Transactional
	public void deleteArticle(String id, String contextPath) {
		Article article = articleRepository.findOne(id);
		if(StringUtils.isNotBlank(article.getContentResource())) {
			String[] paths = article.getContentResource().split(",");
			for(String path :paths) {
				System.out.println(contextPath+"/"+path);
				File file = new File(contextPath+"/"+path);
				file.delete();
			}
		}
		if(article.getAttachments()!=null && article.getAttachments().size()>0) {
			for(ArticleAttachment attr : article.getAttachments()) {
				String path = attr.getDisk()+attr.getFilePath();
				File file = new File(path);
				file.delete();
			}
		}
		
		articleRepository.delete(id);
	}

	@Override
	public ArticleAttachment findAttchById(String id) {
		return articleAttachmentRepository.findOne(id);
	}

	@Override
	@Transactional
	public void deleteAttachment(String id) {
		ArticleAttachment attr = articleAttachmentRepository.findOne(id);
		String path = attr.getDisk()+attr.getFilePath();
		File file = new File(path);
		file.delete();
		articleAttachmentRepository.delete(id);
	}

	

}
