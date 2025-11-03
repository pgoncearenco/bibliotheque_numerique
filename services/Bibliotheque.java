package services;

import exceptions.*;
import models.Emprunt;
import models.Media;
import models.Membre;

import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Bibliotheque implements Serializable {
    private static final long serialVersionUID = 1L;

    private static transient Bibliotheque INSTANCE; // Singleton
    private static transient final Logger LOG = Logger.getLogger(Bibliotheque.class.getName());

    // Modèle de données
    @SuppressWarnings("FieldMayBeFinal")
    private Map<Integer, Media> medias = new HashMap<>();
    @SuppressWarnings("FieldMayBeFinal")
    private Map<Integer, Membre> membres = new HashMap<>();
    @SuppressWarnings("FieldMayBeFinal")
    private List<Emprunt> historique = new ArrayList<>();

    // Persistance
    private transient PersistenceService persistence;

    // Compteurs persistés (IDs internes)
    private int nextIdMediaGlobal = 1;
    private int nextIdMembre = 1;

    // Séquences de codes jolis (affichage)
    private int seqLivre = 1;
    private int seqMagazine = 1;
    private int seqCD = 1;
    private int seqDVD = 1;
    private int seqMembre = 1;

    private Bibliotheque() {}

    public static synchronized Bibliotheque getInstance(String cheminFichier) {
        if (INSTANCE == null) {
            INSTANCE = new Bibliotheque();
            INSTANCE.persistence = new PersistenceService(cheminFichier);
            Bibliotheque chargee = INSTANCE.persistence.charger();
            if (chargee != null) {
                INSTANCE = chargee;
                INSTANCE.persistence = new PersistenceService(cheminFichier);
            }
        }
        return INSTANCE;
    }

    private void autosave() {
        if (persistence != null) persistence.sauvegarder(this);
    }

    // ========= Génération d'IDs / Codes =========
    public synchronized int genererIdMediaInterne() { return nextIdMediaGlobal++; }
    public synchronized int genererIdMembreInterne() { return nextIdMembre++; }

    public synchronized String genererCodeMedia(String type) {
        String t = type == null ? "" : type.trim().toLowerCase();
        return switch (t) {
            case "livre"    -> "L" + String.format("%04d", seqLivre++);
            case "magazine" -> "M" + String.format("%04d", seqMagazine++);
            case "cd"       -> "C" + String.format("%04d", seqCD++);
            case "dvd"      -> "D" + String.format("%04d", seqDVD++);
            default         -> "X" + String.format("%04d", nextIdMediaGlobal);
        };
    }

    public synchronized String genererCodeMembre() {
        return "MB" + String.format("%04d", seqMembre++);
    }

    // ========= Accès données =========
    public Map<Integer, Media> getMedias() { return medias; }
    public Map<Integer, Membre> getMembres() { return membres; }
    public List<Emprunt> getHistorique() { return historique; }

    // ========= Gestion des médias =========
    @SuppressWarnings("LoggerStringConcat")
    public void ajouterMedia(Media m) throws InvalidMediaException {
        if (m == null) throw new InvalidMediaException("Media null");
        if (medias.containsKey(m.getId())) throw new InvalidMediaException("ID media déjà utilisé: " + m.getId());
        medias.put(m.getId(), m);
        LOG.info("Ajout media: " + m);
        autosave();
    }

    @SuppressWarnings("LoggerStringConcat")
    public void supprimerMedia(int id) throws MediaNonTrouveException {
        Media removed = medias.remove(id);
        if (removed == null) throw new MediaNonTrouveException("Media introuvable: " + id);
        LOG.info("Suppression media: " + removed);
        autosave();
    }

    @SuppressWarnings("LoggerStringConcat")
    public void modifierMedia(Media m) throws MediaNonTrouveException {
        if (!medias.containsKey(m.getId())) throw new MediaNonTrouveException("Media introuvable: " + m.getId());
        medias.put(m.getId(), m);
        LOG.info("Modification media: " + m);
        autosave();
    }

    // ========= Gestion des membres =========
    @SuppressWarnings("LoggerStringConcat")
    public void inscrireMembre(Membre m) throws InvalidMediaException {
        if (m == null) throw new InvalidMediaException("Membre null");
        if (membres.containsKey(m.getIdMembre())) throw new InvalidMediaException("ID membre déjà utilisé");
        membres.put(m.getIdMembre(), m);
        LOG.info("Inscription membre: " + m);
        autosave();
    }

    public Membre getMembre(int id) throws MembreNonTrouveException {
        Membre m = membres.get(id);
        if (m == null) throw new MembreNonTrouveException("Membre introuvable: " + id);
        return m;
    }

    public void modifierNomMembre(int idMembre, String nouveauNom) throws MembreNonTrouveException {
        Membre m = membres.get(idMembre);
        if (m == null) throw new MembreNonTrouveException("Membre introuvable avec ID: " + idMembre);
        m.setNom(nouveauNom);
        autosave();
    }

    // ========= Emprunts / retours =========
    @SuppressWarnings("LoggerStringConcat")
    public Emprunt emprunter(int idMedia, int idMembre, Date dateEmprunt) throws BibliothequeException {
        try {
            GestionnaireEmprunt ge = new GestionnaireEmprunt(medias, membres, historique);
            Emprunt e = ge.emprunter(idMedia, idMembre, dateEmprunt);
            LOG.info("Emprunt: media #" + idMedia + " par membre #" + idMembre);
            autosave();
            return e;
        } catch (BibliothequeException be) {
            LOG.log(Level.WARNING, "Erreur emprunt", be);
            throw be;
        }
    }

    @SuppressWarnings("LoggerStringConcat")
    public double rendre(int idMedia, int idMembre, Date dateRetour) throws BibliothequeException {
        try {
            GestionnaireEmprunt ge = new GestionnaireEmprunt(medias, membres, historique);
            double amende = ge.rendre(idMedia, idMembre, dateRetour);
            LOG.info("Retour: media #" + idMedia + " par membre #" + idMembre + " amende=" + amende + "€");
            autosave();
            return amende;
        } catch (BibliothequeException be) {
            LOG.log(Level.WARNING, "Erreur retour", be);
            throw be;
        }
    }

    // ========= Recherche =========
    public List<Media> rechercherTitre(String q) { return new RechercheService(medias).parTitre(q); }
    public List<Media> rechercherAuteur(String a) { return new RechercheService(medias).parAuteur(a); }
    public List<Media> rechercherCategorie(String c) { return new RechercheService(medias).parCategorie(c); }
    public List<Media> lister(boolean disponibles) { return new RechercheService(medias).disponibles(disponibles); }
    public List<Media> tous() { return new RechercheService(medias).tous(); }
}
