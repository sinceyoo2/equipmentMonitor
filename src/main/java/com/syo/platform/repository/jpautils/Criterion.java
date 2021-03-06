package com.syo.platform.repository.jpautils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/** 
 * 条件接口 
 * 用户提供条件表达式接口 
 * @Class Name Criterion 
 * @Author lee 
 */  
public interface Criterion {  
    public enum Operator {  
        EQ, NE, LIKE, GT, LT, GTE, LTE, AND, OR  
    }  
    public Predicate toPredicate(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder builder);  
}  
