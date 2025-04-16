package com.eni.encheres.dao;

import com.eni.encheres.bo.Categorie;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class DAOCategorieMock implements IDAOCategorie {

    private static final List<Categorie> categories = new ArrayList<>();

    static {
        categories.add(new Categorie(1, "Informatique"));
        categories.add(new Categorie(2, "Livres"));
        categories.add(new Categorie(3, "Vêtements"));
        categories.add(new Categorie(4, "Maison"));
    }

    @Override
    public Categorie trouveParId(long id) {
        return categories.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Categorie trouveParLibelle(String libelle) {
        return null;
    }

    public static Categorie trouveParLibelleMock(String libelle) {
        return categories.stream()
                .filter(c -> Objects.equals(c.getLibelle(), libelle))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Categorie> trouveTout() {
        return new ArrayList<>(categories);
    }
}
