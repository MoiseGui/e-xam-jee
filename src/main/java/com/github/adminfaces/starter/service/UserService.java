package com.github.adminfaces.starter.service;

import com.github.adminfaces.starter.infra.dao.UserDao;
import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.model.SortOrder;
import com.github.adminfaces.starter.model.Role;
import com.github.adminfaces.starter.model.User;
import com.github.adminfaces.template.exception.BusinessException;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.UpdateOperations;
import org.omnifaces.util.Faces;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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
    UserDao userDao;

    List<User> allUsers;

    @PostConstruct
    public void init() {
        allUsers = userDao.findAll();
    }

    public String showUsersSize(){
        return "Nombre d'utilisateurs : " + allUsers.size();
    }

    public User findById(String id) {
        User user = allUsers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new BusinessException("Utilisateur non trouvé avec l'id " + id));
        return userDao.read(user.getObjectId());
    }

    public User findUserById(String id){
//        return userDao.read(new ObjectId(id));
        return userDao.findById(id);
    }

    public long count() {
//        return allUsers.stream()
//                .filter(configFilter(filter).stream()
//                        .reduce(Predicate::or).orElse(t -> true))
//                .count();
        return userDao.getCount();
    }

    public long countEtudiants() {
        long count = 0;
        for (User user : allUsers) {
            if (user.getRole().equals(Role.etu)) {
                count++;
            }
        }
        return count;
    }

    public long countEnseignants() {
        long count = 0;
        for (User user : allUsers) {
            if (user.getRole().equals(Role.prof)) {
                count++;
            }
        }
        return count;
    }

    public long countAdministrateurs() {
        long count = 0;
        for (User user : allUsers) {
            if (user.getRole().equals(Role.admin)) {
                count++;
            }
        }
        return count;
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

    public List<String> getByEmail(String email) {
        return allUsers.stream().filter(c -> c.getEmail()
                        .toLowerCase().contains(email.toLowerCase()))
                .map(User::getEmail)
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

    public User findUserByEmail(String email){
        return userDao.findByEmail(email);
    }

    public User login(String email, String password) {
        User loadedUser = findUserByEmail(email);
        if (loadedUser != null && loadedUser.getPassword().equals(password)) {
            return loadedUser;
        }
        return null;
    }

    public int insert(User user) {
        validate(user, true);

        if(userDao.findByEmail(user.getEmail()) != null) return -1;

        userDao.create(user);
        allUsers.add(user);
        return 1;
    }

    public int update(User user) {
        validate(user, false);
        User realUser = findById(user.getId());
        User emailUser = findUserByEmail(user.getEmail());
        if(emailUser != null && ! emailUser.getId().equals(realUser.getId())) return -1;
        UpdateOperations<User> ops = userDao.createOperations()
                .set("nom", user.getNom())
                .set("prenom", user.getPrenom())
                .set("email", user.getEmail())
                .set("role", user.getRole())
                .set("username", user.getUsername());
        userDao.update(realUser, ops);
        allUsers = allUsers.stream().map(u -> {
            if(u.getId().equals(user.getId())) {
                return user;
            }
            return u;
        }).collect(Collectors.toList());
        return 1;
    }

    public void remove(User user) {
        userDao.delete(user);
        allUsers.remove(user);
    }

    public void validate(User user, boolean creating) {
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
        if(creating && (user.getPassword() == null || user.getPassword().trim().isEmpty())){
            be.addException(new BusinessException("Vous devez saisir un mot de passe"));
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
