package com.github.adminfaces.starter.bean.exman;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Faces;

import com.github.adminfaces.starter.model.Choix;
import com.github.adminfaces.starter.model.EtudiantExamen;
import com.github.adminfaces.starter.model.Examen;
import com.github.adminfaces.starter.model.ReponsesQuestion;
import com.github.adminfaces.starter.service.ExamenService;

@Named
@SessionScoped
public class ExamenCorrectionMB implements Serializable {
	String examenId;
	Examen examen;
	String etudiant;
	Double note;
	List<ReponsesQuestion> reponsesQuestions = new ArrayList<>();

	@Inject
	ExamenService examenService;

	public void init() throws IOException {

	}

	public void corriger(String examenId, String etudiant) throws IOException {
		System.out.println("examenId= " + examenId);
		System.out.println("etudiant= " + etudiant);
		examen = examenService.findByLibelle(examenId);
		this.etudiant = etudiant;
		note = examen.getEtudiantExamens().stream().filter(e -> e.getEtudiant().equals(etudiant)).findFirst().get()
				.getNote();

		for (int i = 0; i < examen.getQuestions().size(); i++) {
			ReponsesQuestion reponsesQuestion = examen.getQuestions().get(i).getReponses().stream()
					.filter(q -> q.getEtudiant().equals(etudiant)).findFirst().get();
			System.out.println("iiiiiiiiiiiiiiiiiiiiiiiii " + reponsesQuestion.getEtudiant());
			reponsesQuestions.add(reponsesQuestion);
		}
		reponsesQuestions.forEach(reponse ->{
			
			System.out.println("reponse question = " + reponse.toString());
			System.out.println("Les choix");
			Arrays.stream(reponse.getChoix()).forEach(choix->System.out.print("Choix text" + choix));
		});

	Faces.redirect("examen-correction.jsf?id="+examenId+"?etudiant="+etudiant);

	}
	
	public void editNote() {
		
	}

	public List<ReponsesQuestion> getReponsesQuestions() {
		return reponsesQuestions;
	}

	public void setReponsesQuestions(List<ReponsesQuestion> reponsesQuestions) {
		this.reponsesQuestions = reponsesQuestions;
	}

	public String getExamenId() {
		return examenId;
	}

	public void setExamenId(String examenId) {
		this.examenId = examenId;
	}

	public Double getNote() {
		return note;
	}

	public void setNote(Double note) {
		this.note = note;
	}

	public String getEtudiant() {
		return etudiant;
	}

	public void setEtudiant(String etudiant) {
		this.etudiant = etudiant;
	}

	public Examen getExamen() {
		return examen;
	}

	public void setExamen(Examen examen) {
		this.examen = examen;
	}

}