package com.github.adminfaces.starter.model.hbase;

import com.flipkart.hbaseobjectmapper.Family;
import com.flipkart.hbaseobjectmapper.HBColumn;
import com.flipkart.hbaseobjectmapper.HBRecord;
import com.flipkart.hbaseobjectmapper.HBTable;
import com.github.adminfaces.starter.util.StringUtils;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@HBTable(name = "Examens", families = {
        @Family(name = "Info"),
        @Family(name = "Content")
})
public class Examen implements HBRecord<String>, Comparable<Examen> {
    private String id;
    @HBColumn(family = "Info", column = "libelle")
    private String libelle;
    @HBColumn(family = "Info", column = "dateDebut")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateDebut;
    @HBColumn(family = "Info", column = "dateFin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFin;
    @HBColumn(family = "Info", column = "owner")
    private String owner;
    @HBColumn(family = "Content", column = "questions")
    private List<Question> questions;
    @HBColumn(family = "Content", column = "etudiantExamens")
    private List<EtudiantExamen> etudiantExamens;


    public List<Question> getQuestions() {
        if (questions == null) {
            questions = new ArrayList<>();
        }
        return questions;
    }

    public String getId() {
        if (id == null) {
            return "";
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<EtudiantExamen> getEtudiantExamens() {
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

    @Override
    public String composeRowKey() {
        return StringUtils.encodeString(this.libelle);
    }

    @Override
    public void parseRowKey(String s) {
        this.id = s;
    }
}
