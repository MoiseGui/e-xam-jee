package com.github.adminfaces.starter.model;

public class Choix {
    private String choixTexte;
    private Boolean bonneReponse;
    private Integer Ordre;

    public Choix(String choixTexte, Boolean bonneReponse, Integer ordre) {
        this.choixTexte = choixTexte;
        this.bonneReponse = bonneReponse;
        Ordre = ordre;
    }

    public Choix() {
    }

    public String getChoixTexte() {
        return choixTexte;
    }

    public void setChoixTexte(String choixTexte) {
        this.choixTexte = choixTexte;
    }

    public Boolean getBonneReponse() {
        return bonneReponse;
    }

    public void setBonneReponse(Boolean bonneReponse) {
        this.bonneReponse = bonneReponse;
    }

    public Integer getOrdre() {
        return Ordre;
    }

    public void setOrdre(Integer ordre) {
        Ordre = ordre;
    }
}
