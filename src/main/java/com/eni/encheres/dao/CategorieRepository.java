package com.eni.encheres.dao;

import com.eni.encheres.bo.ArticleVendu;
import com.eni.encheres.bo.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CategorieRepository extends JpaRepository<Categorie, Long> {
    Optional<Categorie> findByLibelle(String libelle);
}
