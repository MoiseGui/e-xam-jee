package com.github.adminfaces.starter.model;

import com.github.adminfaces.starter.util.PersistentEntity;
import org.mongodb.morphia.annotations.Entity;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity(value = "examens", noClassnameStored = true)
public class Examen extends PersistentEntity {
    private String libelle;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateDebut;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFin;

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
}
