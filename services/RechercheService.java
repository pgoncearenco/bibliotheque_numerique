package services;

import models.Media;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RechercheService {
    private final Map<Integer, Media> medias;

    public RechercheService(Map<Integer, Media> medias) {
        this.medias = medias;
    }

    public Media parId(int id) {
        return medias.get(id);
    }

    public List<Media> parTitre(String query) {
        String q = (query == null) ? "" : query.toLowerCase(Locale.ROOT);
        List<Media> res = new ArrayList<>();
        for (Media m : medias.values()) {
            if (m.getTitre().toLowerCase(Locale.ROOT).contains(q)) res.add(m);
        }
        return res;
    }

    public List<Media> parAuteur(String auteur) {
        String q = (auteur == null) ? "" : auteur.toLowerCase(Locale.ROOT);
        List<Media> res = new ArrayList<>();
        for (Media m : medias.values()) {
            String a = (m.getAuteur() == null) ? "" : m.getAuteur().toLowerCase(Locale.ROOT);
            if (a.contains(q)) res.add(m);
        }
        return res;
    }

    // "Catégorie" = type de classe (Livre, Magazine, CD, DVD)
    public List<Media> parCategorie(String categorie) {
        String q = (categorie == null) ? "" : categorie.toLowerCase(Locale.ROOT);
        List<Media> res = new ArrayList<>();
        for (Media m : medias.values()) {
            if (m.getClass().getSimpleName().toLowerCase(Locale.ROOT).contains(q)) res.add(m);
        }
        return res;
    }

    public List<Media> tous() {
        return new ArrayList<>(medias.values());
    }

    public List<Media> disponibles(boolean dispo) {
        List<Media> res = new ArrayList<>();
        for (Media m : medias.values()) if (m.isDisponible() == dispo) res.add(m);
        return res;
    }
}
