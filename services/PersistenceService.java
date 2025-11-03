package services;

import java.io.*;
import java.util.logging.Logger;

public class PersistenceService {
    private final File fichier;
    private static final Logger LOG = Logger.getLogger(PersistenceService.class.getName());

    public PersistenceService(String cheminFichier) {
        this.fichier = new File(cheminFichier);
    }

    public void sauvegarder(Bibliotheque biblio) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichier))) {
            oos.writeObject(biblio);
            LOG.info("État sauvegardé dans " + fichier.getAbsolutePath());
        } catch (NotSerializableException nse) {
            LOG.severe("Classe non sérialisable: " + nse.getMessage());
        } catch (IOException e) {
            LOG.severe("Erreur de sérialisation: " + e.getMessage());
        }
    }

    public Bibliotheque charger() {
        if (!fichier.exists()) return null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichier))) {
            Object obj = ois.readObject();
            if (obj instanceof Bibliotheque) {
                LOG.info("État restauré depuis " + fichier.getAbsolutePath());
                return (Bibliotheque) obj;
            }
        } catch (Exception e) {
            LOG.severe("Erreur de désérialisation: " + e.getMessage());
        }
        return null;
    }
}
