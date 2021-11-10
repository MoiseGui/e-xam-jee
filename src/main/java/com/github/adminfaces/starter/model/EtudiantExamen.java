package com.github.adminfaces.starter.model;

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
}
