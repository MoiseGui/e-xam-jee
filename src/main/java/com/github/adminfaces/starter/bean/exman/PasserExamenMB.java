package com.github.adminfaces.starter.bean.exman;

import com.github.adminfaces.starter.infra.security.LogonMB;
import com.github.adminfaces.starter.model.*;
import com.github.adminfaces.starter.service.ExamenService;
import com.github.adminfaces.starter.service.UserService;
import com.github.adminfaces.starter.util.TypeQuestion;
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
import java.util.*;

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
    ExamenService examenService;
    @Inject
    LogonMB logonMB;
    String answer = "";
    @Inject
    UserService userService;
    @Inject ExamenListMB examenListMB;

    List<ReponsesQuestion> reponseQuestions = new ArrayList();


    public void init() throws IOException {
        if (Faces.isAjaxRequest()) {
            return;
        }
//        if(examen == null) {
        if (has(id)) {
            examen = examenService.findByID(id);
            hydrateReponseQuestion();
        } else
            Faces.redirect(adminConfig.getIndexPage());
//        }

        if (examen != null) {
            User user = userService.findUserById(examen.getOwner());

            if (user != null) {
                ownerName = user.getPrenom() + " " + user.getNom();
            }
        }

    }


    public void submitAnswers() throws IOException {
        Double note = this.calculateScore();
        EtudiantExamen etudiantExamen = new EtudiantExamen();
        etudiantExamen.setEtudiant(logonMB.getCurrentUser().getPrenom() + " " + logonMB.getCurrentUser().getNom());
        etudiantExamen.setDateDebut(examen.getDateDebut());
        etudiantExamen.setDateFin(new Date());
        etudiantExamen.setNote(note);
        for (int i = 0; i < examen.getQuestions().size(); i++) {
            Question question = examen.getQuestions().get(i);
            ReponsesQuestion reponseQuestion = reponseQuestions.get(i);
            question.getReponses().add(reponseQuestion);
        }
        examen.getEtudiantExamens().add(etudiantExamen);
        examen.setTotal(getTotalScore());
        examenService.update(examen);
        examenListMB.refresh();
        Faces.redirect("examen-result.jsf");

    }

    public String timer() {
        Date startTime = examen.getDateDebut();
        Date endTime = examen.getDateFin();
        return String.valueOf(endTime.getTime() - startTime.getTime());
    }

    public Double getTotalScore() {
        return examen.getQuestions().stream().mapToDouble(Question::getPoints).sum();
    }

    public void hydrateReponseQuestion() {
        if (reponseQuestions.isEmpty()) {
            examen.getQuestions().forEach(question -> {
                ReponsesQuestion reponseQuestion = new ReponsesQuestion();
                reponseQuestion.setVraiOuFaux(null);
                reponseQuestion.setEtudiant(logonMB.getCurrentUser().getPrenom() + " " + logonMB.getCurrentUser().getNom());
                reponseQuestion.setChoix(new String[question.getChoix().size()]);
                System.out.println("reponseQuestion.getChoix().length = " + reponseQuestion.getChoix().length);
                reponseQuestions.add(reponseQuestion);
            });
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

    public Double calculateScore() {
        // loop sur les questions
        int index = 0;
        int note = 0;
        for (Question question : examen.getQuestions()) {
            ReponsesQuestion reponseQuestion = reponseQuestions.get(index);
            if (question.getTypeQuestion().equals(TypeQuestion.CHOIX_MULTIPLE)) {
                boolean reponseCorrecte = true;
                for (int i = 0; i < question.getChoix().size(); i++) {
                    if (Arrays.asList(reponseQuestion.getChoix()).contains(question.getChoix().get(i).getChoixTexte()) && question.getChoix().get(i).getBonneReponse().equals("false")) {
                        reponseCorrecte = false;
                    }
                }
                if (reponseCorrecte) {
                    note += question.getPoints();
                }
            } else if (question.getTypeQuestion().equals(TypeQuestion.CHOIX_UNIQUE)) {
                boolean reponseCorrecte = true;
                for (int i = 0; i < question.getChoix().size(); i++) {
                    if (reponseQuestion.getChoix()[0].equals(question.getChoix().get(i).getChoixTexte()) && question.getChoix().get(i).getBonneReponse().equals("false")) {
                        reponseCorrecte = false;
                    }
                }
                if (reponseCorrecte) {
                    note += question.getPoints();
                }
            } else if (question.isVraiOuFaux()) {
                if (Objects.equals(reponseQuestion.getVraiOuFaux(), Boolean.valueOf(question.getChoix().get(0).getBonneReponse()))) {
                    note += question.getPoints();
                }
            }
            index++;
        }
        return (double) note;
    }

    public void onTimeout() {
        // action to be done after the expiration of ...
    }

    public List<Question> getOrderedQuestions() {
        return this.getExamen().getQuestions();
    }

    public boolean canPasssExam() {
        if(examen.getDateDebut().before(new Date()) && examen.getDateFin().after(new Date())){
            if(examen.getEtudiantExamens().stream().noneMatch(e -> e.getEtudiant().equals(logonMB.getCurrentUser().getPrenom()+" "+logonMB.getCurrentUser().getNom()))) return true;
        }
        return false;
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

    public List<ReponsesQuestion> getReponseQuestions() {
        return reponseQuestions;
    }

    public void setReponseQuestions(List<ReponsesQuestion> reponseQuestions) {
        this.reponseQuestions = reponseQuestions;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
