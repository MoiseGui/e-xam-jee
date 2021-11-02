package com.github.adminfaces.starter.service;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.model.SortOrder;
import com.github.adminfaces.starter.model.Car;
import com.github.adminfaces.starter.model.User;
import com.github.adminfaces.template.exception.BusinessException;
import org.mongodb.morphia.Datastore;

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
public class UserService implements Serializable {

    @Inject
    @RequestScoped
    private Datastore datastore;

    @Inject
    List<User> allUsers;

    public String showUsersSize(){
        return "Nombre d'utilisateurs : " + allUsers.size();
    }

    public User findBylogin(String login) {
        return null;
    }

    public int signIn(String login, String password) {
        return 0;
    }

    public User findById(String id) {
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

    public List<String> getByNom(String query) {
        return allUsers.stream().filter(c -> c.getNom()
                        .toLowerCase().contains(query.toLowerCase()))
                .map(User::getNom)
                .collect(Collectors.toList());
    }

    public User findByUsername(String username) {
        return allUsers.stream()
                .filter(c -> c.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public User findByEmail(String email) {
        return allUsers.stream()
                .filter(c -> c.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    public User login(String email, String password) {
        User loadedUser = findByEmail(email);
        if (loadedUser != null && loadedUser.getPassword().equals(password)) {
            return loadedUser;
        }
        return null;
    }

    public void insert(User user) {
        validate(user);
        /*user.setId(allUsers.stream()
                .mapToInt(c -> c.getId())
                .max()
                .getAsInt() + 1);*/
        allUsers.add(user);
    }

    public void update(User user) {
        validate(user);
        allUsers.remove(allUsers.indexOf(user));
        allUsers.add(user);
    }

    public void remove(User user) {
        allUsers.remove(user);
    }

    public void validate(User user) {
        BusinessException be = new BusinessException();
        if (user.getEmail() == null && "".equals(user.getEmail().trim())){
            be.addException(new BusinessException("Vous devez saisir un email"));
        }
        if (user.getNom() == null && "".equals(user.getNom().trim())) {
            be.addException(new BusinessException("Vous devez saisir un nom"));
        }
        if (user.getPrenom() == null && "".equals(user.getPrenom().trim())) {
            be.addException(new BusinessException("Vous devez saisir un prénom"));
        }
        if (user.getUsername() == null && "".equals(user.getUsername().trim())) {
            be.addException(new BusinessException("Vous devez saisir un username"));
        }

//        if (allUsers.stream().filter(c -> c.getEmail().equalsIgnoreCase(user.getEmail())
//                && c.getId() != c.getId()).count() > 0) {
//            be.addException(new BusinessException("Car name must be unique"));
//        }
        if (has(be.getExceptionList())) {
            throw be;
        }
    }
}
