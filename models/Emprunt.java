package models;

import java.io.Serializable;
import java.util.Date;

public class Emprunt implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Media media;
    private final Membre membre;
    private final Date dateEmprunt;
    private final Date dateRetourPrevue;
    private Date dateRetourReelle;
    private double amende; // 1€ / jour de retard

    public Emprunt(Media media, Membre membre, Date dateEmprunt, Date dateRetourPrevue) {
        this.media = media;
        this.membre = membre;
        this.dateEmprunt = dateEmprunt;
        this.dateRetourPrevue = dateRetourPrevue;
    }

    public Media getMedia() { return media; }
    public Membre getMembre() { return membre; }
    public Date getDateEmprunt() { return dateEmprunt; }
    public Date getDateRetourPrevue() { return dateRetourPrevue; }
    public Date getDateRetourReelle() { return dateRetourReelle; }
    public double getAmende() { return amende; }

    public void marquerRetour(Date dateRetourReelle, long joursRetard) {
        this.dateRetourReelle = dateRetourReelle;
        this.amende = Math.max(0, joursRetard) * 1.0; // 1€/jour
    }
}
