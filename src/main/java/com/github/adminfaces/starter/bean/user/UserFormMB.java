/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.starter.bean.user;

import com.github.adminfaces.starter.model.hbase.User;
import com.github.adminfaces.starter.service.UserService;
import com.github.adminfaces.starter.servicehbase.UserServiceHbase;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;

import javax.faces.application.FacesMessage;
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
    UserServiceHbase userService;

    public void init() throws IOException {
        if (Faces.isAjaxRequest()) {
            return;
        }
        if (has(id)) {
            try {
                user = userService.findById(id);
                if(user == null){
                    addDetailMessage("Utilisateur inexistant.");
                    Faces.getFlash().setKeepMessages(true);
                    Faces.redirect("user-list.jsf");
                }
            } catch (Exception e) {
                addDetailMessage("Utilisateur inexistant.");
                Faces.getFlash().setKeepMessages(true);
                Faces.redirect("user-list.jsf");
            }

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


    public void remove() throws Exception {
        if (has(user) && !user.getId().isEmpty()) {
            userService.remove(user);
            addDetailMessage("Utilisateur " + user.getEmail()
                    + " supprimé avec succès.");
            Faces.getFlash().setKeepMessages(true);
            Faces.redirect("user-list.jsf");
        }
    }

    public void save() throws Exception {
        String msg;
        int result = 0;
        if (user.getId().isEmpty()) {
            result = userService.insert(user);
            if(result < 0){
                msg = "Un utilisateur existe déjà avec cette adresse mail";
                addDetailMessage(msg, FacesMessage.SEVERITY_ERROR);
            }
            else msg = "Utilisateur " + user.getEmail() + " créé avec succès";
        } else {
            result = userService.update(user);
            if(result < 0){
                msg = "Un utilisateur existe déjà avec cette adresse mail";
                addDetailMessage(msg, FacesMessage.SEVERITY_ERROR);
            }
            else msg = "Utilisateur " + user.getEmail() + " modifié avec succès";
        }
        if(result >= 0) {
            addDetailMessage(msg);
            Faces.getFlash().setKeepMessages(true);
            Faces.redirect("user-list.jsf");
        }
    }

    public void clear() {
        user = new User();
        id = null;
    }

    public boolean isNew() {
        return user == null || user.getId().isEmpty();
    }

}
