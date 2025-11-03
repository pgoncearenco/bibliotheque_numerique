package models;

import java.util.Date;

public class CD extends Media implements Empruntable {
    private int duree; // en secondes

    // Suivi d'emprunt
    @SuppressWarnings("unused")
    private Membre empruntePar;
    @SuppressWarnings("unused")
    private Date dateEmprunt;
    @SuppressWarnings("unused")
    private Date dateRetourPrevue;

    public CD(int id, String titre, String artiste, int anneePublication, boolean disponible, int duree) {
        super(id, titre, artiste, anneePublication, disponible);
        this.duree = duree;
    }

    public int getDuree() { return duree; }
    public void setDuree(int duree) { this.duree = duree; }

    @Override
    public boolean emprunter(Membre m, Date dateEmprunt) {
        if (!isDisponible() || m == null) return false;
        this.empruntePar = m;
        this.dateEmprunt = dateEmprunt;
        this.dateRetourPrevue = new Date(dateEmprunt.getTime() + 14L * 24 * 60 * 60 * 1000);
        setDisponible(false);
        return true;
    }

    @Override
    public boolean rendre(Date dateRetour) {
        if (isDisponible()) return false;
        this.empruntePar = null;
        this.dateEmprunt = null;
        this.dateRetourPrevue = null;
        setDisponible(true);
        return true;
    }

    @Override
    public boolean isEmpruntable() { return true; }

    @Override
    public void afficherDetails() {
        System.out.println("--- CD ---");
        System.out.println((getCode() == null ? "" : (getCode() + " / ")) + "ID interne: " + getId());
        System.out.println("\t" + getTitre() + " (" + getAnneePublication() + ") [" + (isDisponible() ? "disponible" : "emprunté") + "]");
        System.out.println(" Artiste : " + (getAuteur() == null ? "" : getAuteur()));
        System.out.println(" Durée   : " + duree + " min");
    }
}
