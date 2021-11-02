package com.github.adminfaces.starter.service;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.model.SortOrder;
import com.github.adminfaces.starter.model.User;
import com.github.adminfaces.starter.model.User;
import com.github.adminfaces.starter.model.User;
import com.github.adminfaces.template.exception.BusinessException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.mongodb.morphia.Datastore;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.github.adminfaces.template.util.Assert.has;

@Stateless
public class UserService   implements Serializable {

    @Inject
    @RequestScoped
    private Datastore datastore;

    List<User> allUsers;

    @PostConstruct
    private void init(){
        allUsers = datastore.createQuery(User.class).asList();
        System.out.println("********************************");
        allUsers.forEach(System.out::println);
        System.out.println("********************************");
    }

    public User findBylogin(String login) {
        return null;
    }

    public int signIn(String login, String password) {
        return 0;
    }

    public User findById(Integer id) {
        return allUsers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new BusinessException("Utilisateur non trouvé avec l'id " + id));
    }
    
    public long count(Filter<User> filter) {
        return allUsers.stream()
                .filter(configFilter(filter).stream()
                        .reduce(Predicate::or).orElse(t -> true))
                .count();
    }

    private List<Predicate<User>> configFilter(Filter<User> filter) {
        List<Predicate<User>> predicates = new ArrayList<>();
        if (filter.hasParam("id")) {
            Predicate<User> idPredicate = (User c) -> c.getId().equals(filter.getParam("id"));
            predicates.add(idPredicate);
        }
        
        return predicates;
    }

    public List<User> paginate(Filter<User> filter) {
        List<User> pagedUsers = new ArrayList<>();
        if (has(filter.getSortOrder()) && !SortOrder.UNSORTED.equals(filter.getSortOrder())) {
            pagedUsers = allUsers.stream().
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
            pagedUsers = pagedUsers.subList(filter.getFirst(), page > allUsers.size() ? allUsers.size() : page);
            return pagedUsers;
        }

        List<Predicate<User>> predicates = configFilter(filter);

        List<User> pagedList = allUsers.stream().filter(predicates
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
}
