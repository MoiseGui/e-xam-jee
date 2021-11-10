package com.github.adminfaces.starter.model;

import org.mongodb.morphia.annotations.Embedded;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

public class ReponsesQuestion {
    private String etudiant;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEnvoi;
    private String reponse;
    private boolean vraiOuFaux;
    @Embedded
    private List<Choix> choix;

    public String getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(String etudiant) {
        this.etudiant = etudiant;
    }

    public Date getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(Date dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    public boolean isVraiOuFaux() {
        return vraiOuFaux;
    }

    public void setVraiOuFaux(boolean vraiOuFaux) {
        this.vraiOuFaux = vraiOuFaux;
    }

    public List<Choix> getChoix() {
        return choix;
    }

    public void setChoix(List<Choix> choix) {
        this.choix = choix;
    }
}
