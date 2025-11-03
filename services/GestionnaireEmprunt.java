package services;

import exceptions.MediaDejaEmprunteException;
import exceptions.MediaNonTrouveException;
import exceptions.MembreNonTrouveException;
import models.Empruntable;
import models.Media;
import models.Emprunt;
import models.Membre;
import utils.DateUtils;

import java.util.*;

public class GestionnaireEmprunt {
    private final Map<Integer, Media> medias;
    private final Map<Integer, Membre> membres;
    private final List<Emprunt> historique;

    public GestionnaireEmprunt(Map<Integer, Media> medias, Map<Integer, Membre> membres, List<Emprunt> historique) {
        this.medias = medias;
        this.membres = membres;
        this.historique = historique;
    }

    public Emprunt emprunter(int idMedia, int idMembre, Date dateEmprunt)
            throws MediaNonTrouveException, MediaDejaEmprunteException, MembreNonTrouveException {

        Media media = medias.get(idMedia);
        if (media == null) throw new MediaNonTrouveException("Media introuvable: " + idMedia);
        if (!(media instanceof Empruntable)) throw new MediaDejaEmprunteException("Ce média n'est pas empruntable.");
        if (!media.isDisponible()) throw new MediaDejaEmprunteException("Média déjà emprunté.");

        Membre membre = membres.get(idMembre);
        if (membre == null || !membre.isActif()) throw new MembreNonTrouveException("Membre introuvable/inactif: " + idMembre);
        if (!membre.peutEncoreEmprunter()) throw new MediaDejaEmprunteException("Limite de 5 emprunts atteinte.");

        boolean ok = ((Empruntable) media).emprunter(membre, dateEmprunt);
        if (!ok) throw new MediaDejaEmprunteException("Impossible d'emprunter le média.");

        // déterminer date retour prévue : on lit l'état depuis le type ; pour l'historique on met +14 j par défaut
        Date retourPrevu = DateUtils.plusJours(dateEmprunt, 14);
        Emprunt e = new Emprunt(media, membre, dateEmprunt, retourPrevu);
        membre.getEmpruntsActuels().add(e);
        historique.add(e);
        return e;
    }

    public double rendre(int idMedia, int idMembre, Date dateRetour)
            throws MediaNonTrouveException, MembreNonTrouveException {

        Media media = medias.get(idMedia);
        if (media == null) throw new MediaNonTrouveException("Media introuvable: " + idMedia);
        if (!(media instanceof Empruntable)) return 0.0;

        Membre membre = membres.get(idMembre);
        if (membre == null) throw new MembreNonTrouveException("Membre introuvable: " + idMembre);

        boolean ok = ((Empruntable) media).rendre(dateRetour);
        if (!ok) return 0.0;

        // trouver l'emprunt courant
        Emprunt courant = null;
        for (Emprunt e : membre.getEmpruntsActuels()) {
            if (e.getMedia().getId() == idMedia) { courant = e; break; }
        }
        if (courant != null) {
            long joursRetard = DateUtils.joursDeRetard(dateRetour, courant.getDateRetourPrevue());
            courant.marquerRetour(dateRetour, joursRetard);
            membre.getEmpruntsActuels().remove(courant);
            membre.getHistorique().add(courant);
            return courant.getAmende();
        }
        return 0.0;
    }
}
