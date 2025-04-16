package com.eni.encheres.service;

import com.eni.encheres.bo.Categorie;
import com.eni.encheres.dao.IDAOCategorie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategorieService {

    @Autowired
    private IDAOCategorie daoCategorie;

    public Categorie trouveParId(long id) {
        return daoCategorie.trouveParId(id);
    }

    public List<Categorie> getAll() {
        return daoCategorie.trouveTout();
    }
}
