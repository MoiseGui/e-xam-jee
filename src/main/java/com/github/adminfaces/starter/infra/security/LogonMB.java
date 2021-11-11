package com.github.adminfaces.starter.infra.security;

import com.github.adminfaces.starter.model.User;
import com.github.adminfaces.starter.service.ExamenService;
import com.github.adminfaces.starter.service.UserService;
import com.github.adminfaces.template.session.AdminSession;
import org.omnifaces.util.Faces;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Specializes;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import com.github.adminfaces.template.config.AdminConfig;
import javax.inject.Inject;

/**
 * Created by MoiseGui
 *
 * This is just a login example.
 *
 * AdminSession uses isLoggedIn to determine if user must be redirect to login page or not.
 * By default AdminSession isLoggedIn always resolves to true so it will not try to redirect user.
 *
 * If you already have your authorization mechanism which controls when user must be redirect to initial page or logon
 * you can skip this class.
 */
@Named
@SessionScoped
@Specializes
public class LogonMB extends AdminSession implements Serializable {

    private User currentUser;
    private String email;
    private String password;
    private boolean remember;
    @Inject
    private AdminConfig adminConfig;

    @Inject
    UserService userService;

    @Inject
    ExamenService examenService;


    public void login() throws IOException {
        currentUser = userService.login(email, password);
        if(currentUser != null){
//            userService.init();
            examenService.init();
            addDetailMessage("Vous êtes connecté en tant que <b>" + currentUser.getPrenom() + " " + currentUser.getNom() + "</b>");
            Faces.getExternalContext().getFlash().setKeepMessages(true);
            Faces.redirect(adminConfig.getIndexPage());
        }
        else {
            Faces.getExternalContext().getFlash().setKeepMessages(true);
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login ou Mot de passe incorrect", "Login ou Mot de passe incorrect");
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
    }

    public boolean loginFailed(){
        return currentUser == null;
    }

    @Override
    public boolean isLoggedIn() {

        return currentUser != null;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRemember() {
        return remember;
    }

    public void setRemember(boolean remember) {
        this.remember = remember;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
