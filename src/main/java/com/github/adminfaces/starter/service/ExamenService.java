package com.github.adminfaces.starter.service;

import com.github.adminfaces.starter.infra.dao.ExamenDao;
import com.github.adminfaces.starter.infra.dao.UserDao;
import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.model.SortOrder;
import com.github.adminfaces.starter.model.Examen;
import com.github.adminfaces.starter.model.User;
import com.github.adminfaces.template.exception.BusinessException;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.github.adminfaces.template.util.Assert.has;

@Stateless
public class ExamenService {
    @Inject
    private ExamenDao examenDao;
    List<Examen> examens;

    @PostConstruct
    public void init() {
        this.examens = examenDao.findAll();
        System.out.println("examens.size() = " + examens.size());
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

    public Examen findByLibelle(String libelle) {
        Optional<Examen> foundExam = examens.stream()
                .filter(examen -> examen.getLibelle().equals(libelle))
                .findFirst();
        return foundExam.orElseThrow(() -> new BusinessException("Examen non trouvé par libelle " + libelle));
    }

    public long count() {
//        return allUsers.stream()
//                .filter(configFilter(filter).stream()
//                        .reduce(Predicate::or).orElse(t -> true))
//                .count();
        return examenDao.getCount();
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
                            return c1.getId().compareTo(c2.getId());
                        } else {
                            return c2.getId().compareTo(c1.getId());
                        }
                    })
                    .collect(Collectors.toList());
        }

        int page = filter.getFirst() + filter.getPageSize();
        if (filter.getParams().isEmpty()) {
            pagedExamens = pagedExamens.subList(filter.getFirst(), page > examens.size() ? examens.size() : page);
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
        examens.add(examen);
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
        if (has(be.getExceptionList())) {
            throw be;
        }
    }

}
