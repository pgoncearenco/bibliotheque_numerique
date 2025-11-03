package ui;

import exceptions.BibliothequeException;
import exceptions.InvalidMediaException;
import exceptions.MembreNonTrouveException;
import models.CD;
import models.DVD;
import models.Livre;
import models.Magazine;
import models.Media;
import models.Membre;
import services.Bibliotheque;
import utils.DateUtils;
import utils.ValidationUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private final Bibliotheque biblio;
    private final Scanner sc = new Scanner(System.in);

    public Menu(Bibliotheque biblio) {
        this.biblio = biblio;
    }

    @SuppressWarnings("UseSpecificCatch")
    public void demarrer() {
        while (true) {
            System.out.println("\n======== BIBLIOTHEQUE NUMERIQUE ========");
            System.out.println("\t1) Ajouter un media");
            System.out.println("\t2) Lister les medias disponibles");
            System.out.println("\t3) Lister les medias empruntes");
            System.out.println("\t4) Rechercher (titre/auteur/categorie)");
            System.out.println("\t5) Inscrire un membre");
            System.out.println("\t6) Emprunter un media");
            System.out.println("\t7) Rendre un media");
            System.out.println("\t8) Statistiques");
            System.out.println("\t9) Modifier nom membre");
            System.out.println("\t10) Lister les membres");
            System.out.println("\t0) Quitter");
            System.out.print("Choix: ");

            String c = sc.nextLine().trim();
            try {
                switch (c) {
                    case "1" -> ajouterMedia();
                    case "2" -> afficherMedias(true);
                    case "3" -> afficherMedias(false);
                    case "4" -> rechercher();
                    case "5" -> inscrireMembre();
                    case "6" -> emprunter();
                    case "7" -> rendre();
                    case "8" -> stats();
                    case "9" -> modifierNomMembre();
                    case "10" -> listerMembres();
                    case "0" -> { System.out.println("Au revoir !"); return; }
                    default -> { System.out.println("Choix invalide."); pause(); }
                }
            } catch (Exception e) {
                System.out.println("Erreur: " + e.getMessage());
                pause();
            }
        }
    }

    private void ajouterMedia() throws InvalidMediaException, ParseException {
        System.out.println("Type du media :");
        System.out.println("\t1) Livre");
        System.out.println("\t2) Magazine");
        System.out.println("\t3) CD");
        System.out.println("\t4) DVD");
        System.out.print("Choix: ");
        String choix = sc.nextLine().trim();

        String type;
        switch (choix) {
            case "1" -> type = "livre";
            case "2" -> type = "magazine";
            case "3" -> type = "cd";
            case "4" -> type = "dvd";
            default -> {
                System.out.println("Choix invalide (1, 2, 3 ou 4 attendu).");
                pause();
                return;
            }
        }

        int idInterne = biblio.genererIdMediaInterne();     // ID auto
        String code = biblio.genererCodeMedia(type);        // Code joli

        System.out.print("Titre: ");
        String titre = sc.nextLine().trim();
        System.out.print("Auteur/Real./Artiste: ");
        String auteur = sc.nextLine().trim();
        System.out.print("Annee: ");
        int annee = Integer.parseInt(sc.nextLine().trim());

        Media m;
        switch (type) {
            case "livre" -> {
                System.out.print("Nb pages: ");
                int pages = Integer.parseInt(sc.nextLine().trim());
                m = new Livre(idInterne, titre, auteur, annee, true, pages);
                m.setCode(code);
            }
            case "magazine" -> {
                System.out.print("Numero edition: ");
                int num = Integer.parseInt(sc.nextLine().trim());
                System.out.print("Date parution (yyyy-MM-dd): ");
                Date parution = DateUtils.parse(sc.nextLine().trim());
                m = new Magazine(idInterne, titre, auteur, annee, true, num, parution);
                m.setCode(code);
            }
            case "cd" -> {
                System.out.print("Duree (minutes): ");
                int duree = Integer.parseInt(sc.nextLine().trim());
                m = new CD(idInterne, titre, auteur, annee, true, duree);
                m.setCode(code);
            }
            case "dvd" -> {
                System.out.print("Format (DVD/Blu-Ray/4K): ");
                String format = sc.nextLine().trim();
                m = new DVD(idInterne, titre, auteur, annee, true, format);
                m.setCode(code);
            }
            default -> { // sécurité
                System.out.println("Type inconnu.");
                pause();
                return;
            }
        }

        biblio.ajouterMedia(m);
        System.out.println("Ajout OK -> [" + code + "] " + m + "  (ID interne: " + idInterne + ")");
        System.out.println("Total medias: " + biblio.tous().size());
        pause();
    }

    private void afficherMedias(boolean disponibles) {
        List<Media> list = biblio.lister(disponibles);
        if (list.isEmpty()) {
            System.out.println(disponibles ? "Aucun media disponible." : "Aucun media emprunte.");
        } else {
            System.out.println(disponibles ? "\nMEDIAS DISPONIBLES :" : "\nMEDIAS EMPRUNTES :");
            list.forEach(Media::afficherDetails);
        }
        pause();
    }

    private void rechercher() {
        System.out.print("Filtre (titre/auteur/categorie): ");
        String f = sc.nextLine().trim().toLowerCase();
        System.out.print("Valeur du filtre choisi: ");
        String v = sc.nextLine().trim();
        List<Media> res = switch (f) {
            case "titre" -> biblio.rechercherTitre(v);
            case "auteur" -> biblio.rechercherAuteur(v);
            case "categorie" -> biblio.rechercherCategorie(v);
            default -> biblio.tous();
        };
        if (res.isEmpty()) System.out.println("Aucun resultat.");
        else {
            System.out.println("RESULTATS :");
            res.forEach(Media::afficherDetails);
        }
        pause();
    }

    private void inscrireMembre() throws InvalidMediaException {
        int id = biblio.genererIdMembreInterne();    // ID auto
        String code = biblio.genererCodeMembre();    // Code joli

        System.out.print("Nom complet: ");
        String nom = sc.nextLine().trim();
        System.out.print("Email: ");
        String email = sc.nextLine().trim();
        ValidationUtils.assureEmail(email);

        Membre m = new Membre(id, nom, email, new Date());
        biblio.inscrireMembre(m);
        System.out.println("Membre inscrit -> [" + code + "] " + m + "  (ID interne: " + id + ")");
        System.out.println("Total membres: " + biblio.getMembres().size());
        pause();
    }

    private void modifierNomMembre() throws MembreNonTrouveException {
        System.out.print("ID membre (interne) : ");
        int id = Integer.parseInt(sc.nextLine().trim());
        System.out.print("Nouveau nom : ");
        String nom = sc.nextLine().trim();
        biblio.modifierNomMembre(id, nom);
        System.out.println("Nom mis a jour !");
        pause();
    }

    private void listerMembres() {
        if (biblio.getMembres().isEmpty()) {
            System.out.println("Aucun membre inscrit.");
            pause();
            return;
        }
        System.out.println("\nMEMBRES INSCRITS :");
        biblio.getMembres().values().forEach(m -> {
            System.out.println("\t- " + m.toString()); // ex: "Jean <jean@mail> (#1)"
        });
        System.out.println("Total membres: " + biblio.getMembres().size());
        pause();
    }

    private void emprunter() throws BibliothequeException, ParseException {
        System.out.print("ID media (interne): ");
        int idMedia = Integer.parseInt(sc.nextLine().trim());

    // Vérification côté UI : le média doit exister et être empruntable
        models.Media media = biblio.getMedias().get(idMedia);
        if (media == null) {
            System.out.println("Media introuvable avec cet ID.");
            pause();
            return;
        }
        if (!(media instanceof models.Empruntable)) {
            System.out.println("Ce media n'est pas empruntable (ex: DVD). Choisissez un Livre, Magazine ou CD.");
            pause();
            return;
        }
        if (!media.isDisponible()) {
            System.out.println("Ce media est déjà emprunté.");
            pause();
            return;
        }

        System.out.print("ID membre (interne): ");
        int idMembre = Integer.parseInt(sc.nextLine().trim());

        System.out.print("Date emprunt (yyyy-MM-dd) [vide=aujourdhui]: ");
        String d = sc.nextLine().trim();
        Date date = d.isBlank() ? new Date() : DateUtils.parse(d);

        biblio.emprunter(idMedia, idMembre, date);
        System.out.println("Emprunt realise.");
        pause();
    }


    private void rendre() throws BibliothequeException, ParseException {
        System.out.print("ID media (interne): ");
        int idMedia = Integer.parseInt(sc.nextLine().trim());
        System.out.print("ID membre (interne): ");
        int idMembre = Integer.parseInt(sc.nextLine().trim());
        System.out.print("Date retour (yyyy-MM-dd) [vide=aujourdhui]: ");
        String d = sc.nextLine().trim();
        Date date = d.isBlank() ? new Date() : DateUtils.parse(d);
        double amende = biblio.rendre(idMedia, idMembre, date);
        System.out.println("Retour effectue. Amende = " + amende + "€");
        pause();
    }

    private void stats() {
        System.out.println("\n--- STATISTIQUES ---");
        try {
            int nbMedias = biblio.tous().size();
            int nbMembres = biblio.getMembres().size();
            long nbEmpruntes = biblio.tous().stream().filter(m -> !m.isDisponible()).count();

            System.out.println("\tMedias totaux     : " + nbMedias);
            System.out.println("\tMembres           : " + nbMembres);
            System.out.println("\tEmprunts en cours : " + nbEmpruntes);
        } catch (Exception e) {
            System.out.println("Erreur stats: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
        System.out.println("--------------------");
        pause();
    }

    private void pause() {
        System.out.print("Appuyez sur Entree pour continuer...");
        sc.nextLine();
    }
}
