/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.starter.bean.exman;

import com.github.adminfaces.starter.infra.security.LogonMB;
import com.github.adminfaces.starter.model.Choix;
import com.github.adminfaces.starter.model.Examen;
import com.github.adminfaces.starter.model.Question;
import com.github.adminfaces.starter.service.ExamenService;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;
import org.primefaces.PrimeFaces;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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
    private boolean editChoice = false;
    private boolean addQuestion = true;


    public boolean isAddQuestion() {
        return addQuestion;
    }

    public void setAddQuestion(boolean addQuestion) {
        this.addQuestion = addQuestion;
    }

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
        System.out.println("showAddQuestionModal : Opening the dialog");
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", false);
        PrimeFaces.current().dialog().openDynamic("dialogs/addQuestionDialog", options, null);

    }

    public void save() throws IOException {
        String msg = "";
        if (examen.getDateDebut().after(examen.getDateFin())) {
            msg = "La date de début doit être antérieure à la date de fin";
            addDetailMessage(msg, FacesMessage.SEVERITY_ERROR);
            Faces.getFlash().setKeepMessages(true);
        } else {
            if (examen.getId() == null || examen.getId().isEmpty()) {
                examen.setQuestions(getQuestions());
                examen.setOwner(logonMB.getCurrentUser().getId());
                examenService.insert(examen);
                msg = "Examen " + examen.getLibelle() + " créé avec succès";
            } else {
                int result = examenService.update(examen);
                if (result < 1) {
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
        if (choiceToAdd == null) {
            choiceToAdd = new Choix();
        }
        return choiceToAdd;
    }

    public void setChoiceToAdd(Choix choiceToAdd) {
        this.choiceToAdd = choiceToAdd;
    }

    public void addQuestion() {
        PrimeFaces current = PrimeFaces.current();
        if (questionToAdd != null) {
            System.out.println("hello");
            long count = examen.getQuestions().stream().filter(q -> q.getTitre().equals(questionToAdd.getTitre())).count();
            getQuestionsToAdd().forEach(q -> {
                System.out.println("q.getTitre() : " + q.getTitre());
            });
            System.out.println("count : " + count);
            if (count > 0) {
                System.out.println("Question already exists");
                String msg = "Cette question existe déjà";
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null);
                FacesContext.getCurrentInstance().addMessage(null, message);
                questionToAdd = new Question();
                return;
            }
            long countOrders = examen.getQuestions().stream().filter(q -> q.getOrdre() == questionToAdd.getOrdre()).count();
            if (countOrders > 0) {
                String msg = "Vous ne peuvez pas ajouter deux questions avec le même ordre";
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null);
                FacesContext.getCurrentInstance().addMessage(null, message);
                questionToAdd = new Question();
                return;
            }
            if (editingQuestion) {
                examen.setQuestions(examen
                        .getQuestions()
                        .stream()
                        .map(question -> question.getTitre().equals(questionToAdd.getTitre()) ? questionToAdd : question)
                        .collect(Collectors.toList()));
            } else {
                examen.getQuestions().add(questionToAdd);
            }
            current.executeScript("PF('addQuestion').hide();");
            questionToAdd = new Question();
        }
        editingQuestion = false;
        String msg = "Question crée avec succès !!";
        addDetailMessage(msg);
    }

    public void addChoice() {
        if (questionToAdd.getTypeQuestion().equals("VraiFaux")) {
            questionToAdd.setVraiOuFaux(choiceToAdd.getBonneReponse().equals("true"));
        }
        if (questionToAdd.getTypeQuestion().equals("choixUnique")) {
            long correctChoices = questionToAdd.getChoix().stream().filter(choix -> choix.getBonneReponse().equals(choiceToAdd.getBonneReponse())).count();
            System.out.println("the number of correct choices is " + correctChoices);
            if (correctChoices > 0) {
                String msg = "Vous ne pouvez pas avoir plus d'une bonne réponse pour une question de type vrai ou faux";
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null);
                FacesContext.getCurrentInstance().addMessage(null, message);
                System.out.println("*********************************");
                questionToAdd.getChoix().forEach(choix -> System.out.println(choix.getBonneReponse()));
                System.out.println("*********************************");
                choiceToAdd = new Choix();
                return;
            }
        }
        if (questionToAdd.getTypeQuestion().equals("choixUnique") || questionToAdd.getTypeQuestion().equals("choixMultiple")) {
            long orderDuplicates = questionToAdd.getChoix().stream().filter(choix -> choix.getOrdre().equals(choiceToAdd.getOrdre())).count();
            if (orderDuplicates > 0) {
                String msg = "Vous ne pouvez pas avoir deux choix avec le même ordre";
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null);
                FacesContext.getCurrentInstance().addMessage(null, message);
                choiceToAdd = new Choix();
                return;
            }
        }
        if (editChoice) {
            addChoiceToQuestion(questionToAdd, choiceToAdd);
            editChoice = false;
        } else {
            questionToAdd.getChoix().add(choiceToAdd);
        }

        choiceToAdd = new Choix();
    }

    public void addChoiceToQuestion(Question question, Choix choiceToAdd) {
        question
                .setChoix(questionToAdd.getChoix().stream()
                        .map(choice -> choice.getOrdre().equals(choiceToAdd.getOrdre()) ? choiceToAdd : choice)
                        .collect(Collectors.toList()));

    }

    public void fillFormToEdit(Question question) {
        PrimeFaces current = PrimeFaces.current();
        current.executeScript("PF('addQuestion').show();");
        editingQuestion = true;
        questionToAdd = question;
    }

    public void removeQuestion(Question question) {
        List<Question> questionsToKeep = examen.getQuestions().stream().filter(q -> q.getOrdre() != question.getOrdre()).collect(Collectors.toList());
        examen.setQuestions(questionsToKeep);
    }

    public void removeChoice(Choix choix) {
        List<Choix> choixToKeep = questionToAdd.getChoix().stream().filter(q -> !q.getOrdre().equals(choix.getOrdre())).collect(Collectors.toList());
        questionToAdd.setChoix(choixToKeep);
    }

    public void fillChoiceFormToEdit(Choix choix) {

        editChoice = true;
        choiceToAdd = choix;
    }

    public boolean isEditChoice() {
        return editChoice;
    }

    public void emptyQuestionAddForm() {
        questionToAdd = new Question();
    }

    public void setEditChoice(boolean editChoice) {
        this.editChoice = editChoice;
    }
}
