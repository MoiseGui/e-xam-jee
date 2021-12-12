package com.github.adminfaces.starter.model;

import java.io.Serializable;

public class Choix implements Serializable {
    private String choixTexte;
    private String bonneReponse;
    private Integer ordre;

    public Choix(String choixTexte, String bonneReponse, Integer ordre) {
        this.choixTexte = choixTexte;
        this.bonneReponse = bonneReponse;
        this.ordre = ordre;
    }

    public Choix() {
        choixTexte = "";
    }

    public String getChoixTexte() {
        return choixTexte;
    }

    public void setChoixTexte(String choixTexte) {
        this.choixTexte = choixTexte;
    }

    public String getBonneReponse() {
        if (bonneReponse == null) {
            bonneReponse = "false";
        }
        return bonneReponse;
    }

    public void setBonneReponse(String bonneReponse) {
        this.bonneReponse = bonneReponse;
    }

    public Integer getOrdre() {

        return ordre;
    }

    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
    }
}
