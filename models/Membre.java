package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Membre implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int idMembre;
    private String nom;
    @SuppressWarnings("FieldMayBeFinal")
    private String email;
    @SuppressWarnings("FieldMayBeFinal")
    private Date dateInscription;
    private boolean actif = true;

    @SuppressWarnings("FieldMayBeFinal")
    private ArrayList<Emprunt> empruntsActuels = new ArrayList<>();
    @SuppressWarnings("FieldMayBeFinal")
    private ArrayList<Emprunt> historique = new ArrayList<>();

    public Membre(int idMembre, String nom, String email, Date dateInscription) {
        this.idMembre = idMembre;
        this.nom = nom;
        this.email = email;
        this.dateInscription = (dateInscription == null) ? new Date() : dateInscription;
    }

    public int getIdMembre() { return idMembre; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getEmail() { return email; }
    public Date getDateInscription() { return dateInscription; }
    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }
    public ArrayList<Emprunt> getEmpruntsActuels() { return empruntsActuels; }
    public ArrayList<Emprunt> getHistorique() { return historique; }

    public boolean peutEncoreEmprunter() { return empruntsActuels.size() < 5; }

    @Override
    public String toString() { return nom + " <" + email + "> (#" + idMembre + ")"; }
}
