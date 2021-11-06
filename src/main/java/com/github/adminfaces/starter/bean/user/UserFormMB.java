/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.starter.bean.user;

import com.github.adminfaces.starter.model.User;
import com.github.adminfaces.starter.model.User;
import com.github.adminfaces.starter.service.UserService;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import static com.github.adminfaces.template.util.Assert.has;

/**
 * @author MoiseGui
 */
@Named
@ViewScoped
public class UserFormMB implements Serializable {


    private String id;
    private User user;

    @Inject
    UserService userService;

    public void init() {
        if(Faces.isAjaxRequest()){
           return;
        }
        if (has(id)) {
            user = userService.findById(id);
        } else {
            user = new User();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public void remove() throws IOException {
        if (has(user) && has(user.getId())) {
            userService.remove(user);
            addDetailMessage("Utilisateur " + user.getEmail()
                    + " supprimé avec succès.");
            Faces.getFlash().setKeepMessages(true);
            Faces.redirect("user-list.jsf");
        }
    }

    public void save() {
        String msg;
        if (user.getId() == null) {
            userService.insert(user);
            msg = "Utilisateur " + user.getEmail() + " créé avec succès";
        } else {
            userService.update(user);
            msg = "Utilisateur " + user.getEmail() + " modifié avec succès";
        }
        addDetailMessage(msg);
    }

    public void clear() {
        user = new User();
        id = null;
    }

    public boolean isNew() {
        return user.getId() == null;
    }

}
