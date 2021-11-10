package com.github.adminfaces.starter.bean.user;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.model.User;
import com.github.adminfaces.starter.service.UserService;
import com.github.adminfaces.template.exception.BusinessException;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;


@Named
@ViewScoped
public class UserListMB implements Serializable {
    @Inject
    UserService userService;

    LazyDataModel<User> users;

    Filter<User> filter = new Filter<>(new User());

    List<User> selectedUsers;

    List<User> filteredValue;

    String id;
    String email;

    @PostConstruct
    public void initDataModel(){
        users = new LazyDataModel<User>() {
            @Override
            public List<User> load(int first, int pageSize,
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
                List<User> list = userService.paginate(filter);
                setRowCount((int) userService.count(filter));
                return list;

            }

            @Override
            public int getRowCount() {
                return super.getRowCount();
            }

            @Override
            public User getRowData(String key) {
                return userService.findById(key);
            }
        };
    }

    public void findUserById(String id) {
        if (id == null) {
            throw new BusinessException("Provide User ID to load");
        }
        selectedUsers.add(userService.findById(id));
    }

    public void findUserByEmail(String email) {
        if (email == null) {
            throw new BusinessException("Aucun résultat");
        }
        selectedUsers.add(userService.findByEmail(email));
    }

    public List<String> completeNom(String query) {
        List<String> result = userService.getByNom(query);
        return result;
    }

    public List<String> completeEmail(String query) {
        List<String> result = userService.getByEmail(query);
        return result;
    }

    public void delete() {
        int numUsers = 0;
        for (User selectedUser : selectedUsers) {
            userService.remove(selectedUser);
            numUsers++;
        }
        selectedUsers.clear();
        addDetailMessage(numUsers + " utilisateur supprimé avec succès !");
    }

    public Filter<User> getFilter() {
        return filter;
    }

    public void setFilter(Filter<User> filter) {
        this.filter = filter;
    }

    public List<User> getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedUsers(List<User> selectedUsers) {
        this.selectedUsers = selectedUsers;
    }

    public List<User> getFilteredValue() {
        return filteredValue;
    }

    public void setFilteredValue(List<User> filteredValue) {
        this.filteredValue = filteredValue;
    }

    public LazyDataModel<User> getUsers() {
        return users;
    }

    public void setUsers(LazyDataModel<User> users) {
        this.users = users;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
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
