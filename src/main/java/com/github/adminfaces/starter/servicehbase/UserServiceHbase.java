package com.github.adminfaces.starter.servicehbase;

import com.github.adminfaces.starter.infra.daohbase.UserDao;
import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.model.SortOrder;
import com.github.adminfaces.starter.model.hbase.Role;
import com.github.adminfaces.starter.model.hbase.User;
import com.github.adminfaces.template.exception.BusinessException;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.github.adminfaces.template.util.Assert.has;

@Stateless
public class UserServiceHbase implements Serializable {

    @Inject
    UserDao userDao;

    List<User> allUsers;

    @PostConstruct
    public void init() throws IOException {
        allUsers = userDao.findAll();
    }

    public String showUsersSize() {
        return "Nombre d'utilisateurs : " + allUsers.size();
    }

    public User findById(String id) throws IOException {
        return userDao.findById(id);
    }

    public User findUserById(String id) throws IOException {
        return userDao.findById(id);
    }

    public long count() {
        return allUsers.size();
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

    /* public User findUserByEmail(String email) {
        return userDao.findByEmail(email);
    } */

    public User login(String email, String password) {
        User loadedUser = findByEmail(email);
        if (loadedUser != null && loadedUser.getPassword().equals(password)) {
            return loadedUser;
        }
        return null;
    }

    public int insert(User user) throws Exception {
        validate(user, true);

        if (findByEmail(user.getEmail()) != null) return -1;

        userDao.save(user);
        allUsers.add(user);
        return 1;
    }

    public int update(User user) throws Exception {
        validate(user, false);
        User realUser = findById(user.getId());
        User emailUser = findByEmail(user.getEmail());
        if (emailUser != null && !emailUser.getId().equals(realUser.getId())) return -1;
        userDao.update(realUser);
        allUsers = allUsers.stream().map(u -> {
            if (u.getId().equals(user.getId())) {
                return user;
            }
            return u;
        }).collect(Collectors.toList());
        return 1;
    }

    public void remove(User user) throws Exception {
        userDao.delete(user);
        allUsers.remove(user);
    }

    public void validate(User user, boolean creating) {
        BusinessException be = new BusinessException();
        if (user.getEmail() == null && "".equals(user.getEmail().trim())) {
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
        if (creating && (user.getPassword() == null || user.getPassword().trim().isEmpty())) {
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
