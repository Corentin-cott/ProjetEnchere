package com.eni.encheres.dao;

import com.eni.encheres.bo.Categorie;
import java.util.List;

public interface IDAOCategorie {
    Categorie trouveParId(long id);
    Categorie trouveParLibelle(String libelle);
    List<Categorie> trouveTout();
}
