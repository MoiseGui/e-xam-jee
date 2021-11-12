package com.github.adminfaces.starter.model;


import com.github.adminfaces.starter.util.PersistentEntity;
import org.mongodb.morphia.annotations.Entity;

@Entity(value = "users", noClassnameStored = true)
public class User extends PersistentEntity {
    private String nom;
    private String prenom;
    private String username;
    private String email;
    private String password;
    private String cne;
    private String role;


    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getCne() {
        if (cne == null) {
            return "";
        }
        return cne;
    }

    public void setCne(String cne) {
        this.cne = cne;
    }

    public String getRole() {
        if (role == null) {
            return Role.etu;
        }
        return role;
    }

    public void setRole(String role) {
        if (role == null) {
            this.role = Role.etu;
            return;
        }
        this.role = role;
    }

    public boolean isAdmin() { return this.getRole().equals(Role.admin); }

    public boolean isProfesseur() {
        return this.getRole().equals(Role.prof);
    }

    public boolean isEtudiant() { return this.getRole().equals(Role.etu); }
}
