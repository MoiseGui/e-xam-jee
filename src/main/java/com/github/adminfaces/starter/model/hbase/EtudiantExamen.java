package com.github.adminfaces.starter.model.hbase;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

public class EtudiantExamen {
    private String etudiant;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateDebut;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFin;
    private Double note;

    public EtudiantExamen() {
    }

    public EtudiantExamen(String etudiant, Date dateDebut, Date dateFin, Double note) {
        this.etudiant = etudiant;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.note = note;
    }

    public String getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(String etudiant) {
        this.etudiant = etudiant;
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

    public Double getNote() {
        return note;
    }

    public void setNote(Double note) {
        this.note = note;
    }
}
