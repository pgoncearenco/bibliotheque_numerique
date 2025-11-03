import services.Bibliotheque;
import ui.Menu;

public class Main {
    public static void main(String[] args) {
        // Fichier de persistance local au projet
        Bibliotheque b = Bibliotheque.getInstance("bibliotheque.dat");
        new Menu(b).demarrer();
    }
}
