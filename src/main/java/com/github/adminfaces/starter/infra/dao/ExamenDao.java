package com.github.adminfaces.starter.infra.dao;

import com.github.adminfaces.starter.model.EtudiantExamen;
import com.github.adminfaces.starter.model.Examen;
import com.mongodb.WriteResult;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;

import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class ExamenDao {
    @Inject
    Datastore datastore;

    public Key<Examen> create(Examen entity) {
        return datastore.save(entity);
    }

    public long getCount() {
        return datastore.getCount(Examen.class);
    }

    public long getCount(String userId) {
        return datastore.find(Examen.class, "owner", userId).countAll();
    }

    public List<Examen> findAll() {
        return datastore.createQuery(Examen.class).order("created").asList();
    }

    public List<Examen> findAll(String userId) {
        return datastore.find(Examen.class, "owner", userId).order("created").asList();
    }

    public List<Examen> findAllExamensEtudiant(String etudiant) {
        EtudiantExamen filter = new EtudiantExamen();
        filter.setEtudiant(etudiant);
        List<Examen> examens = datastore.find(Examen.class)
                .field("etudiantExamens")
                .hasThisElement(filter)
                .asList();
        for (Examen examen : examens) {
            examen.getEtudiantExamens().removeIf(etudiantExam -> !etudiantExam.getEtudiant().equals(etudiant));
        }
        return examens;
    }

    public Examen findById(String id) {
        return datastore.get(Examen.class, new ObjectId(id));
    }

    public Examen findByLibelle(String libelle) {
        return datastore.find(Examen.class, "libelle", libelle).get();
    }

    public WriteResult delete(Examen entity) {
        return datastore.delete(entity);
    }

    public UpdateResults update(Examen examen, UpdateOperations<Examen> operations) {
        return datastore.update(examen, operations);
    }

    public UpdateOperations<Examen> createOperations() {
        return datastore.createUpdateOperations(Examen.class);
    }

}
