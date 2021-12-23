package com.github.adminfaces.starter.model.hbase;


import com.flipkart.hbaseobjectmapper.Family;
import com.flipkart.hbaseobjectmapper.HBColumn;
import com.flipkart.hbaseobjectmapper.HBRecord;
import com.flipkart.hbaseobjectmapper.HBTable;

@HBTable(name = "Users", families = {
        @Family(name = "Info")
})
public class User implements HBRecord<String> {
    private String id;
    @HBColumn(family = "Info", column = "nom")
    private String nom;
    @HBColumn(family = "Info", column = "prenom")
    private String prenom;
    @HBColumn(family = "Info", column = "username")
    private String username;
    @HBColumn(family = "Info", column = "email")
    private String email;
    @HBColumn(family = "Info", column = "password")
    private String password;
    @HBColumn(family = "Info", column = "cne")
    private String cne;
    @HBColumn(family = "Info", column = "role")
    private String role;

    public String getId() {
        if (id == null) {
            return "";
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public boolean isAdmin() {
        return this.getRole().equals(Role.admin);
    }

    public boolean isProfesseur() {
        return this.getRole().equals(Role.prof);
    }

    public boolean isEtudiant() {
        return this.getRole().equals(Role.etu);
    }

    @Override
    public String composeRowKey() {
        return email.split("@")[0];
    }

    @Override
    public void parseRowKey(String s) {
        this.id = s;

    }
}
