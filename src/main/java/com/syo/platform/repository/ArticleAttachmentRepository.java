package com.syo.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.syo.platform.entity.ArticleAttachment;

public interface ArticleAttachmentRepository extends JpaRepository<ArticleAttachment, String> {

}
