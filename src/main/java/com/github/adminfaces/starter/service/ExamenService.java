package com.github.adminfaces.starter.service;

import com.github.adminfaces.starter.infra.dao.ExamenDao;
import com.github.adminfaces.starter.infra.dao.UserDao;
import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.model.SortOrder;
import com.github.adminfaces.starter.infra.security.LogonMB;
import com.github.adminfaces.starter.model.Examen;
import com.github.adminfaces.starter.model.User;
import com.github.adminfaces.template.exception.BusinessException;
import org.mongodb.morphia.query.UpdateOperations;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.github.adminfaces.template.util.Assert.has;

@Stateless
public class ExamenService implements Serializable {
    @Inject
    private ExamenDao examenDao;
    List<Examen> examens;

    @Inject
    LogonMB logonMB;

    @PostConstruct
    public void init() {
        if(logonMB.getCurrentUser().isProfesseur()){
            this.examens = examenDao.findAll(logonMB.getCurrentUser().getId());
            return;
        }
        this.examens = examenDao.findAll();
    }

    public List<String> getByLibelle(String query) {
        return examens.stream().filter(c -> c.getLibelle()
                        .toLowerCase().contains(query.toLowerCase()))
                .map(Examen::getLibelle)
                .collect(Collectors.toList());
    }

    public Examen findById(String id) {
        Optional<Examen> foundExam = examens.stream()
                .filter(examen -> examen.getId().equals(id))
                .findFirst();
        System.out.println("l'id de ");
        return foundExam.orElseThrow(() -> new BusinessException("Examen non trouvé par id " + id));
    }

    public Examen findByID(String id) {
        return examenDao.findById(id);
    }

    public Examen findByLibelle(String libelle) {
        Optional<Examen> foundExam = examens.stream()
                .filter(examen -> examen.getLibelle().equals(libelle))
                .findFirst();
        return foundExam.orElseThrow(() -> new BusinessException("Examen non trouvé par libelle " + libelle));
    }

    public Examen findByLibelleDao(String libelle) {
        return examenDao.findByLibelle(libelle);
    }

    public long count() {
        if(logonMB.getCurrentUser().isProfesseur()){
            return examenDao.getCount(logonMB.getCurrentUser().getId());
        }
        return examenDao.getCount();
    }

    public long getPassedExamCountForEtudiant(String idEtudiant) {
        return this.examens.stream().filter(examen -> examen.getEtudiantExamens().stream().anyMatch(etudiantExamen -> etudiantExamen.getEtudiant().equals(idEtudiant))).count();
    }

    public long countCurrentGoingOnExams() {
    	long count = 0;
        Date now = new Date();
        for (Examen examen : examens) {
            if(examen.getDateDebut().before(now) && examen.getDateFin().after(now)) {
                count ++;
            }
        }
        return count;
    }

    public long countFinishedExams(){
        long count = 0;
        Date now = new Date();
        for (Examen examen : examens) {
            if(examen.getDateFin().before(now)) {
                count ++;
            }
        }
        return count;
    }

    public long countCommingExams(){
        long count = 0;
        Date now = new Date();
        for (Examen examen : examens) {
            if(examen.getDateDebut().after(now)) {
                count ++;
            }
        }
        return count;
    }

    private List<Predicate<Examen>> configFilter(Filter<Examen> filter) {
        List<Predicate<Examen>> predicates = new ArrayList<>();
        if (filter.hasParam("id")) {
            Predicate<Examen> idPredicate = (Examen c) -> c.getId().equals(filter.getParam("id"));
            predicates.add(idPredicate);
        }
        return predicates;
    }

    public List<Examen> paginate(Filter<Examen> filter) {
        List<Examen> pagedExamens = new ArrayList<>();
        if (has(filter.getSortOrder()) && !SortOrder.UNSORTED.equals(filter.getSortOrder())) {
            pagedExamens = examens.stream().
                    sorted((c1, c2) -> {
                        if (filter.getSortOrder().isAscending()) {
                            return c1.compareTo(c2);
                        } else {
                            return c2.compareTo(c1);
                        }
                    })
                    .collect(Collectors.toList());
        }

        int page = filter.getFirst() + filter.getPageSize();
        if (filter.getParams().isEmpty()) {
            pagedExamens = pagedExamens.subList(filter.getFirst(), Math.min(page, examens.size()));
            return pagedExamens;
        }

        List<Predicate<Examen>> predicates = configFilter(filter);

        List<Examen> pagedList = examens.stream().filter(predicates
                        .stream().reduce(Predicate::or).orElse(t -> true))
                .collect(Collectors.toList());

        if (page < pagedList.size()) {
            pagedList = pagedList.subList(filter.getFirst(), page);
        }

        if (has(filter.getSortField())) {
            pagedList = pagedList.stream().
                    sorted((c1, c2) -> {
                        boolean asc = SortOrder.ASCENDING.equals(filter.getSortOrder());
                        if (asc) {
                            return c1.getId().compareTo(c2.getId());
                        } else {
                            return c2.getId().compareTo(c1.getId());
                        }
                    })
                    .collect(Collectors.toList());
        }
        return pagedList;
    }

    public void insert(Examen examen) {
        validate(examen);
        examenDao.create(examen);
        examens.add(0, examen);
    }

    public int update(Examen examen) {
        validate(examen);
        Examen realExamen = findById(examen.getId());
        if (realExamen == null) {
            return -1;
        }
        UpdateOperations<Examen> updateOperations = examenDao.createOperations();
        updateOperations.set("libelle", examen.getLibelle());
        updateOperations.set("dateDebut", examen.getDateDebut());
        updateOperations.set("dateFin", examen.getDateFin());
        updateOperations.set("total", examen.getTotal());
        updateOperations.set("questions", examen.getQuestions());
        updateOperations.set("etudiantExamens", examen.getEtudiantExamens());
        examenDao.update(realExamen, updateOperations);
        return 1;
    }

    public void remove(Examen examen) {
        examens.remove(examen);
        examenDao.delete(examen);
    }

    public void validate(Examen examen) {
        BusinessException be = new BusinessException();
        if (examen.getLibelle() == null && "".equals(examen.getLibelle().trim())) {
            be.addException(new BusinessException("Vous devez saisir un email"));
        }
        if(examen.getDateDebut() == null) {
            be.addException(new BusinessException("Vous devez saisir une date de début"));
        }
        if(examen.getDateFin() == null) {
            be.addException(new BusinessException("Vous devez saisir une date de fin"));
        }
        if(examen.getQuestions() == null || examen.getQuestions().isEmpty()) {
            be.addException(new BusinessException("Vous devez ajouter au moins une question à cet examen"));
        }
        if (has(be.getExceptionList())) {
            throw be;
        }
    }

}
