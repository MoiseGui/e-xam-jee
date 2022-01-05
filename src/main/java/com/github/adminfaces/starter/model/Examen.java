package com.github.adminfaces.starter.model;

import com.github.adminfaces.starter.util.PersistentEntity;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(value = "examens", noClassnameStored = true)
public class Examen extends PersistentEntity implements Comparable<Examen> {
    private String libelle;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateDebut;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFin;
    @Embedded
    private List<Question> questions;
    @Embedded
    private List<EtudiantExamen> etudiantExamens;
    private Double total;

    public Double getTotal() {
        return total;
    }

    public int total() {
        if (total == null) {
            total = 0d;
        }
        return total.intValue();
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    private String owner;

    public List<Question> getQuestions() {
        if (questions == null) {
            questions = new ArrayList<>();
        }
        return questions;
    }

    public List<EtudiantExamen> getEtudiantExamens() {
        if (etudiantExamens == null) {
            etudiantExamens = new ArrayList<>();
        }
        return etudiantExamens;
    }

    public void setEtudiantExamens(List<EtudiantExamen> etudiantExamens) {
        this.etudiantExamens = etudiantExamens;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public int compareTo(Examen o) {
        return this.getDateDebut().compareTo(o.getDateDebut());
    }

    public void displayDetails() {
        System.out.println("Examen{" +
                "libelle='" + libelle + '\'' +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", owner='" + owner + '\'' +
                '}');
        this.getQuestions().forEach(q -> {

            System.out.println("Question : " + q.getTitre());
            System.out.println("Le nombre de ponts " + q.getPoints());
            System.out.println("Type de reponse " + q.getTypeQuestion());

            q.getReponses().forEach(r -> {
                System.out.println("l'etudiant concené " + r.getEtudiant());
                System.out.println("la reponse if choix libre" + r.getReponse());
                System.out.println("la reponse if choix libre" + r.getChoix().length);
            });


        });
        this.getEtudiantExamens().forEach(e -> {
            System.out.println("La note de l'etudiant " + e.getEtudiant());

        });

    }
}
