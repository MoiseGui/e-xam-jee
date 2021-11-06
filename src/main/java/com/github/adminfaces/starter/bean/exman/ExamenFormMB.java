/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.starter.bean.exman;

import com.github.adminfaces.starter.model.Examen;
import com.github.adminfaces.starter.model.User;
import com.github.adminfaces.starter.service.ExamenService;
import com.github.adminfaces.starter.service.UserService;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import static com.github.adminfaces.template.util.Assert.has;

/**
 * @author MoiseGui
 */
@Named
@ViewScoped
public class ExamenFormMB implements Serializable {


    private String id;
    private Examen examen;
    private Date dateDebut;
    private Date dateFin;

    @Inject
    ExamenService examenService;

    public void init() {
        if(Faces.isAjaxRequest()){
           return;
        }
        if (has(id)) {
            examen = examenService.findById(id);
            System.out.println("ùùùùùùùùùùùùùùùùùùùùùùùùùùùùùùùùù");
            System.out.println("examen = " + examen.getDateDebut());
            System.out.println("ùùùùùùùùùùùùùùùùùùùùùùùùùùùùùùùùù");
        } else {
           examen = new Examen();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDateDebut() {
        System.out.println("dateDebut (getter)= " + examen.getDateDebut());
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

    public Examen getExamen() {
        return examen;
    }

    public void setExamen(Examen examen) {
        this.examen = examen;
    }

    public void remove() throws IOException {
        if (has(examen) && has(examen.getId())) {
            examenService.remove(examen);
            addDetailMessage("Examen " + examen.getLibelle()
                    + " supprimé avec succès.");
            Faces.getFlash().setKeepMessages(true);
            Faces.redirect("examen-list.jsf");
        }
    }

    public void save() {
        String msg;
        if (examen.getId() == null) {
            examenService.insert(examen);
            msg = "Examen " + examen.getLibelle() + " créé avec succès";
        } else {
            examenService.insert(examen);
            msg = "Examen " + examen.getLibelle() + " modifié avec succès";
        }
        addDetailMessage(msg);
    }

    public void clear() {
        examen = new Examen();
        id = null;
    }

    public boolean isNew() {
        return examen.getId() == null;
    }
    public String formatDate(Date date){
        if(date != null){
            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            String strDate = dateFormat.format(date);
            return strDate;
        }
        return "Date inexistante";

    }
}
