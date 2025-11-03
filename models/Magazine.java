package models;

import utils.DateUtils;
import java.util.Date;

public class Magazine extends Media implements Empruntable {
    private int numeroEdition;
    private Date dateParution;

    // Suivi d'emprunt
    @SuppressWarnings("unused")
    private Membre empruntePar;
    @SuppressWarnings("unused")
    private Date dateEmprunt;
    @SuppressWarnings("unused")
    private Date dateRetourPrevue;

    public Magazine(int id, String titre, String auteur, int anneePublication, boolean disponible,
                    int numeroEdition, Date dateParution) {
        super(id, titre, auteur, anneePublication, disponible);
        this.numeroEdition = numeroEdition;
        this.dateParution = dateParution;
    }

    public int getNumeroEdition() { return numeroEdition; }
    public void setNumeroEdition(int numeroEdition) { this.numeroEdition = numeroEdition; }
    public Date getDateParution() { return dateParution; }
    public void setDateParution(Date dateParution) { this.dateParution = dateParution; }

    @Override
    public boolean emprunter(Membre m, Date dateEmprunt) {
        if (!isDisponible() || m == null) return false;
        this.empruntePar = m;
        this.dateEmprunt = dateEmprunt;
        this.dateRetourPrevue = new Date(dateEmprunt.getTime() + 7L * 24 * 60 * 60 * 1000);
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
        System.out.println("--- Magazine ---");
        System.out.println((getCode() == null ? "" : (getCode() + " / ")) + "ID interne: " + getId());
        System.out.println("\t" + getTitre() + " (" + getAnneePublication() + ") [" + (isDisponible() ? "disponible" : "emprunté") + "]");
        System.out.println(" N° édition : " + numeroEdition);
        System.out.println(" Parution   : " + DateUtils.format(dateParution));
    }
}
