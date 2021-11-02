package com.github.adminfaces.starter.bean.user;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.model.Car;
import com.github.adminfaces.starter.model.User;
import com.github.adminfaces.starter.service.UserService;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Map;


public class UserListMB {
    UserService userService;

    LazyDataModel<User> users;

    Filter<User> filter = new Filter<>(new User());

    List<User> selectedUsers;

    List<User> filteredValue;

    Integer id;

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
                return userService.findById(new Integer(key));
            }
        };
    }
}
