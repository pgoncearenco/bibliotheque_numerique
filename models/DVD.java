package models;

public class DVD extends Media {
    private String format; // DVD / Blu-Ray / 4K

    public DVD(int id, String titre, String realisateur, int anneePublication, boolean disponible, String format) {
        super(id, titre, realisateur, anneePublication, disponible);
        this.format = format;
    }

    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }

    @Override
    public void afficherDetails() {
        System.out.println("--- DVD ---");
        System.out.println((getCode() == null ? "" : (getCode() + " / ")) + "ID interne: " + getId());
        System.out.println("\t" + getTitre() + " (" + getAnneePublication() + ") [" + (isDisponible() ? "disponible" : "emprunté") + "]");
        System.out.println(" Réalisateur : " + (getAuteur() == null ? "" : getAuteur()));
        System.out.println(" Format      : " + (format == null ? "" : format));
    }
}
