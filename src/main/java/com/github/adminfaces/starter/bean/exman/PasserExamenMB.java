package com.github.adminfaces.starter.bean.exman;

import com.github.adminfaces.starter.model.hbase.Examen;
import com.github.adminfaces.starter.model.hbase.Question;
import com.github.adminfaces.starter.model.hbase.User;
import com.github.adminfaces.starter.servicehbase.ExamenServiceHbase;
import com.github.adminfaces.starter.servicehbase.UserServiceHbase;
import com.github.adminfaces.template.config.AdminConfig;
import com.github.adminfaces.template.exception.BusinessException;
import org.bson.types.ObjectId;
import org.omnifaces.util.Faces;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.github.adminfaces.template.util.Assert.has;

@Named
@SessionScoped
public class PasserExamenMB implements Serializable {
    String id;
    String examenId;
    Examen examen;
    String ownerName = "";

    @Inject
    private AdminConfig adminConfig;
    @Inject
    ExamenServiceHbase examenService;

    @Inject
    UserServiceHbase userService;

    public void init() throws IOException {
        if (Faces.isAjaxRequest()) {
            return;
        }
//        if(examen == null) {
        if (has(id))
            examen = examenService.findByID(id);
        else
            Faces.redirect(adminConfig.getIndexPage());
//        }

        if (examen != null) {
            User user = userService.findUserById(examen.getOwner());

            if (user != null) {
                ownerName = user.getPrenom() + " " + user.getNom();
            }
        }

    }

    public void findExamen() throws IOException {
        examen = examenService.findByID(examenId);
        if (examen == null) {
            throw new BusinessException("Examen introuvable " + new ObjectId(examenId));
        } else {
//            addDetailMessage("Examen trouvé " + examenId);
//            Faces.getExternalContext().getFlash().setKeepMessages(true);
            Faces.redirect("examen.jsf?id=" + examenId);
        }
    }

    public void passerExam() throws IOException {
        examen = examenService.findByID(examenId);
        if (examen == null) {
            throw new BusinessException("Examen introuvable " + new ObjectId(examenId));
        } else {
            Faces.redirect("examen-pass.jsf?id=" + examenId);
        }
    }

    public String formatDate(Date date) {
        if (date != null) {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            return dateFormat.format(date);
        }
        return "Date inexistante";
    }

    // retourne la durée en heure:minutes de l'examen
    public String getExamDuration() {
        if (examen != null) {
            // calcul de la durée en minutes
            long duree = examen.getDateFin().getTime() - examen.getDateDebut().getTime();
            long dureeEnMinutes = duree / (60 * 1000);
            // calcul de l'heure
            long heure = dureeEnMinutes / 60;
            // calcul des minutes
            long minutes = dureeEnMinutes % 60;
            if (heure == 0)
                return minutes + " minutes";
            String result = heure + "h ";
            if (minutes > 0)
                result += minutes + "min";
            return result;
        }
        return "";
    }

    public List<Question> getOrderedQuestions() {
        return this.getExamen().getQuestions();
    }

    public boolean canPasssExam() {
        return examen.getDateDebut().before(new Date()) && examen.getDateFin().after(new Date());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExamenId() {
        return examenId;
    }

    public void setExamenId(String examenId) {
        this.examenId = examenId;
    }

    public Examen getExamen() {
        if (examen == null) {
            return new Examen();
        }
        return examen;
    }

    public void setExamen(Examen examen) {
        this.examen = examen;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}
