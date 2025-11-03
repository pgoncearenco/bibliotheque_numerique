package models;

import java.util.Date;

public interface Empruntable {
    boolean emprunter(Membre m, Date dateEmprunt);
    boolean rendre(Date dateRetour);
    boolean isEmpruntable();
}
