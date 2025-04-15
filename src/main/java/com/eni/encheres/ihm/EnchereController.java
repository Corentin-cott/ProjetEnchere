package com.eni.encheres.ihm;

import com.eni.encheres.bo.Enchere;
import com.eni.encheres.service.EnchereService;
import com.eni.encheres.service.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class EnchereController {

    @Autowired
    EnchereService enchereService;

    @GetMapping("/liste-encheres")
    public List<Enchere> getEncheresEnCours() {
        return enchereService.getEncheresEnCours();
    }

    @PostMapping("/encherir")
    public ServiceResponse<String> enchere(@RequestBody Enchere enchere) {
        return enchereService.ajouterEnchere(enchere);
    }

    @GetMapping("/encheres/{idArticle}")
    public List<Enchere> getEncheres(@PathVariable long idArticle) {
        return enchereService.getEncheresParArticle(idArticle);
    }
}
