/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.starter.bean.exman;

import com.github.adminfaces.starter.model.Choix;
import com.github.adminfaces.starter.infra.security.LogonMB;
import com.github.adminfaces.starter.model.Examen;
import com.github.adminfaces.starter.model.Question;
import com.github.adminfaces.starter.model.User;
import com.github.adminfaces.starter.service.ExamenService;
import com.github.adminfaces.starter.service.UserService;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;
import org.primefaces.PrimeFaces;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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
    private Question questionToAdd;
    private Choix choiceToAdd;

    private List<Question> questionsToAdd;
    private List<Choix> choicesToAdd;

    private boolean editingQuestion = false;


    @Inject
    ExamenService examenService;

    @Inject
    LogonMB logonMB;

    public void init() {
        if (Faces.isAjaxRequest()) {
            return;
        }
        if (has(id)) {
            examen = examenService.findById(id);
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

    public void showAddQuestionModal() {
        Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("width", 640);
        options.put("height", 340);
        options.put("contentWidth", "100%");
        options.put("contentHeight", "100%");
        options.put("headerElement", "customheader");

        PrimeFaces.current().dialog().openDynamic("addQuestionModal", options, null);
    }

    public void save() throws IOException {
        String msg = "";
        if(examen.getDateDebut().after(examen.getDateFin())){
            msg = "La date de début doit être antérieure à la date de fin";
            addDetailMessage(msg, FacesMessage.SEVERITY_ERROR);
            Faces.getFlash().setKeepMessages(true);
        }
        else{
            if (examen.getId() == null || examen.getId().isEmpty()) {
                examen.setQuestions(getQuestions());
                examen.setOwner(logonMB.getCurrentUser().getId());
                examenService.insert(examen);
                msg = "Examen " + examen.getLibelle() + " créé avec succès";
            } else {
                int result = examenService.update(examen);
                if(result < 1) {
                    msg = "Erreur lors de la modification de l'examen, Cet examen n'existe peut-être plus";
                    addDetailMessage(msg);
                    return;
                }
                msg = "Examen " + examen.getLibelle() + " modifié avec succès";
            }
            addDetailMessage(msg);
            Faces.getFlash().setKeepMessages(true);
            Faces.redirect("examen-list.jsf");
        }

    }

    public void clear() {
        examen = new Examen();
        id = null;
    }

    public boolean isNew() {
        return examen.getId() == null || examen.getId().isEmpty();
    }

    public String formatDate(Date date) {
        if (date != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            String strDate = dateFormat.format(date);
            return strDate;
        }
        return "Date inexistante";

    }

    public String getQuestionsLength() {
        return examen.getQuestions().size() > 0 ? String.valueOf(examen.getQuestions().size()) : "0";
    }

    public List<Question> getQuestions() {
        System.out.println("the size of the exams in the getter" + examen.getQuestions().size());
        if (examen.getQuestions().size() > 1) {
            return examen.getQuestions().stream().sorted(Question::compareTo).collect(Collectors.toList());
        }
        return examen.getQuestions();
    }

    public Question getQuestionToAdd() {
        if (questionToAdd == null) {
            questionToAdd = new Question();
        }
        return questionToAdd;
    }

    public List<Question> getQuestionsToAdd() {
        if (questionsToAdd == null) {
            questionsToAdd = new ArrayList<>();
        }
        return questionsToAdd;
    }

    public void setQuestionToAdd(Question questionToAdd) {
        this.questionToAdd = questionToAdd;
    }




    public Choix getChoiceToAdd() {
		return choiceToAdd;
	}

	public void setChoiceToAdd(Choix choiceToAdd) {
		this.choiceToAdd = choiceToAdd;
	}

	public void addQuestion() {
        System.out.println("the question i wanna add");
        if (questionToAdd != null) {
            if (editingQuestion) {
                System.out.println("m gonna edit the exam object");
                examen.getQuestions().stream().map(q -> {
                    if (q.getOrdre().equals(questionToAdd.getOrdre())) return questionToAdd;
                    return q;
                });
            } else {
                System.out.println("m gonna add the question to the exams question list");
                examen.getQuestions().add(questionToAdd);
            }

            questionToAdd = new Question();
        }
        editingQuestion = false;
        String msg = "Question crée avec succès !!";
        addDetailMessage(msg);
        System.out.println("*************************");
    }

    public void fillFormToEdit(Question question) {
        editingQuestion = true;
        System.out.println(question.getOrdre());
        questionToAdd = question;
        System.out.println("*****************");
        System.out.println("the question i wanna edit");
    }
    public void removeQuestion(Question question) {
        System.out.println("the question i wanna remove");
        List<Question> questionsToKeep = examen.getQuestions().stream().filter(q -> q.getOrdre() != question.getOrdre()).collect(Collectors.toList());
        questionsToKeep.forEach(q -> System.out.println(q.getOrdre()));
        examen.setQuestions(questionsToKeep);
    }
}
