package models;

import java.io.Serializable;
import java.util.Objects;

public abstract class Media implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int id;
    private String titre;
    private String auteur;
    private int anneePublication;
    private boolean disponible;

    // Nouveau : code lisible (L0001, M0001, C0001, D0001)
    private String code;

    protected Media(int id, String titre, String auteur, int anneePublication, boolean disponible) {
        if (id < 0) throw new IllegalArgumentException("id doit être >= 0");
        this.id = id;
        this.titre = Objects.requireNonNull(titre, "titre");
        this.auteur = auteur;
        this.anneePublication = anneePublication;
        this.disponible = disponible;
    }

    public abstract void afficherDetails();

    // Getters / setters
    public int getId() { return id; }
    public String getTitre() { return titre; }
    public String getAuteur() { return auteur; }
    public int getAnneePublication() { return anneePublication; }
    public boolean isDisponible() { return disponible; }
    public void setTitre(String titre) { this.titre = Objects.requireNonNull(titre); }
    public void setAuteur(String auteur) { this.auteur = auteur; }
    public void setAnneePublication(int anneePublication) { this.anneePublication = anneePublication; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    // Code lisible
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    @Override
    public String toString() {
        return String.format("%d - %s (%d) [%s]", id, titre, anneePublication,
                disponible ? "disponible" : "emprunté");
    }
}
