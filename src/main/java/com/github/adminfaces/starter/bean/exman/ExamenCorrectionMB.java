package com.github.adminfaces.starter.bean.exman;

import java.io.IOException;
import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.omnifaces.util.Faces;

@Named
@SessionScoped
public class ExamenCorrectionMB implements Serializable{
    String examenId;
    int etudiant;

    public void corriger() throws IOException {
        Faces.redirect("examen-correction.jsf");
    }

    public int getEtudiant() {
        return etudiant;
    }

    public String getExamenId() {
        return examenId;
    }

    public void setExamenId(String examenId) {
        this.examenId = examenId;
    }

    public void setEtudiant(int etudiant) {
        this.etudiant = etudiant;
    }
}
