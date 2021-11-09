package com.github.adminfaces.starter.model;

import com.github.adminfaces.starter.util.PersistentEntity;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

@Entity(value = "questions", noClassnameStored = true)
public class Question extends PersistentEntity implements Comparable<Question> {
    private String titre;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    private Integer ordre;
    private Double points;
    private String image;
    private String video;
    private String vraiOuFaux;
    @Embedded
    private List<Choix> choix;

    public Question() {
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Integer getOrdre() {
        return ordre;
    }

    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
    }

    public Double getPoints() {
        return points;
    }

    public void setPoints(Double points) {
        this.points = points;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVraiOuFaux() {
        return vraiOuFaux;
    }

    public void setVraiOuFaux(String vraiOuFaux) {
        this.vraiOuFaux = vraiOuFaux;
    }

    public List<Choix> getChoix() {
        return choix;
    }

    public void setChoix(List<Choix> choix) {
        this.choix = choix;
    }

    @Override
    public int compareTo(Question o) {
        return o.getOrdre().compareTo(this.getOrdre());
    }
}
