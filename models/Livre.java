package models;

import java.util.Date;

public class Livre extends Media implements Empruntable {
    private int nbPages;

    // Suivi d'emprunt
    @SuppressWarnings("unused")
    private Membre empruntePar;
    @SuppressWarnings("unused")
    private Date dateEmprunt;
    @SuppressWarnings("unused")
    private Date dateRetourPrevue;

    public Livre(int id, String titre, String auteur, int anneePublication, boolean disponible, int nbPages) {
        super(id, titre, auteur, anneePublication, disponible);
        this.nbPages = nbPages;
    }

    public int getNbPages() { return nbPages; }
    public void setNbPages(int nbPages) { this.nbPages = nbPages; }

    @Override
    public boolean emprunter(Membre m, Date dateEmprunt) {
        if (!isDisponible() || m == null) return false;
        this.empruntePar = m;
        this.dateEmprunt = dateEmprunt;
        // retour prévu simple : +14 jours
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
        System.out.println("--- Livre ---");
        System.out.println((getCode() == null ? "" : (getCode() + " / ")) + "ID interne: " + getId());
        System.out.println("\t" + getTitre() + " (" + getAnneePublication() + ") [" + (isDisponible() ? "disponible" : "emprunté") + "]");
        System.out.println(" Auteur : " + (getAuteur() == null ? "" : getAuteur()));
        System.out.println(" Pages  : " + nbPages);
    }
}
