package com.eni.encheres.dao;

import com.eni.encheres.bo.ArticleVendu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleVenduRepository extends JpaRepository<ArticleVendu, Long> {
}
