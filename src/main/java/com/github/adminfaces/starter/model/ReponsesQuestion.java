package com.github.adminfaces.starter.model;

import org.mongodb.morphia.annotations.Embedded;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReponsesQuestion {
    private String etudiant;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEnvoi;
    private String reponse;
    private Boolean vraiOuFaux;
    @Embedded
    private String[] choix;

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
        System.out.println("the set response method");
        System.out.println(reponse);
        this.reponse = reponse;
    }

    public Boolean getVraiOuFaux() {
        return vraiOuFaux;
    }

    public void setVraiOuFaux(Boolean vraiOuFaux) {
        this.vraiOuFaux = vraiOuFaux;
    }

    public String[] getChoix() {
        return choix;
    }

    public void setChoix(String[] choix) {
        this.choix = choix;
    }

    @Override
    public String toString() {
        return "ReponsesQuestion{" +
                "etudiant='" + etudiant + '\'' +
                ", dateEnvoi=" + dateEnvoi +
                ", reponse='" + reponse + '\'' +
                ", vraiOuFaux=" + vraiOuFaux +
                ", choix=" + choix +
                '}';
    }
}
