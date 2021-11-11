package com.github.adminfaces.starter.bean.exman;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.model.Examen;
import com.github.adminfaces.starter.service.ExamenService;
import com.github.adminfaces.template.exception.BusinessException;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;


@Named
@ViewScoped
public class ExamenListMB implements Serializable {
    @Inject
    ExamenService examenService;

    LazyDataModel<Examen> examens;

    Filter<Examen> filter = new Filter<>(new Examen());

    List<Examen> selectedExamens;

    List<Examen> filteredExamens;

    String id;
    String email;
    String libelle;

    @PostConstruct
    public void initDataModel() {
        examens = new LazyDataModel<Examen>() {
            @Override
            public List<Examen> load(int first, int pageSize,
                                     String sortField, SortOrder sortOrder,
                                     Map<String, FilterMeta> filters) {
                com.github.adminfaces.starter.infra.model.SortOrder order = null;
                if (sortOrder != null) {
                    order = sortOrder.equals(SortOrder.ASCENDING) ? com.github.adminfaces.starter.infra.model.SortOrder.ASCENDING
                            : sortOrder.equals(SortOrder.DESCENDING) ? com.github.adminfaces.starter.infra.model.SortOrder.DESCENDING
                            : com.github.adminfaces.starter.infra.model.SortOrder.UNSORTED;
                }
                filter.setFirst(first).setPageSize(pageSize)
                        .setSortField(sortField).setSortOrder(order)
                        .setParams(filters);
                List<Examen> list = examenService.paginate(filter);
                setRowCount((int) examenService.count());
                return list;

            }

            @Override
            public int getRowCount() {
                return super.getRowCount();
            }

            @Override
            public Examen getRowData(String key) {
                return examenService.findById(key);
            }
        };
    }

    public List<String> completeNom(String query) {
        List<String> result = examenService.getByLibelle(query);
        return result;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public void findExamenById(String id) {
        if (id == null) {
            throw new BusinessException("Provide User ID to load");
        }
        selectedExamens.add(examenService.findById(id));
    }

    public void findExamenByLibelle(String libelle) {
        System.out.println("libelle = " + libelle);
        if (libelle == null) {
            throw new BusinessException("Provide libelle ID to load");
        }
        selectedExamens.add(examenService.findByLibelle(libelle));
    }


    public void delete() {
        int numUsers = 0;
        for (Examen selectedExamen : selectedExamens) {
            numUsers++;
            examenService.remove(selectedExamen);
        }
        selectedExamens.clear();
        addDetailMessage(numUsers + " Examens supprimé correctement!");
    }

    public long getExamenCount() {
        return examenService.count();
    }

    public long getCurrentGoingOnExams(){
        return examenService.countCurrentGoingOnExams();
    }

    public long countFinishedExams(){
        return examenService.countFinishedExams();
    }

    public long countComingExams(){
        return examenService.countCommingExams();
    }

    public Filter<Examen> getFilter() {
        return filter;
    }

    public void setFilter(Filter<Examen> filter) {
        this.filter = filter;
    }

    public List<Examen> getSelectedExamens() {
        return selectedExamens;
    }

    public void setSelectedExamens(List<Examen> selectedExamens) {
        this.selectedExamens = selectedExamens;
    }

    public List<Examen> getFilteredValue() {
        return filteredExamens;
    }

    public void setFilteredValue(List<Examen> filteredValue) {
        this.filteredExamens = filteredValue;
    }

    public LazyDataModel<Examen> getExamens() {
        return examens;
    }

    public void setExamens(LazyDataModel<Examen> examens) {
        this.examens = examens;
    }

    public List<Examen> getFilteredExamens() {
        return filteredExamens;
    }

    public void setFilteredExamens(List<Examen> filteredExamens) {
        this.filteredExamens = filteredExamens;
    }

    public void setExamenService(ExamenService examenService) {
        this.examenService = examenService;
    }
    public String formatDate(Date date){
        if(date != null){
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String strDate = dateFormat.format(date);
            return strDate;
        }
        return "Date inexistante";

    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
