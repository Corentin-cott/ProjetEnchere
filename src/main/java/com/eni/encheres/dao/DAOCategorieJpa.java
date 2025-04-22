package com.eni.encheres.dao;

import com.eni.encheres.bo.Categorie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DAOCategorieJpa implements IDAOCategorie {

    private final CategorieRepository categorieRepository;

    @Autowired
    public DAOCategorieJpa(CategorieRepository repository) {
        this.categorieRepository = repository;
    }

    @Override
    public Categorie trouveParId(long id) {
        return categorieRepository.findById(id).orElse(null);
    }


//    @Override
//    public Categorie trouveParLibelle(String libelle) {
//        return null; //TODO :Cette méthode ne fait rien
//    }

    @Override
    public List<Categorie> trouveTout() {
        return categorieRepository.findAll();
    }
}
