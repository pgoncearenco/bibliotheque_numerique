package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    private static final String PATTERN = "yyyy-MM-dd";

    public static Date parse(String s) throws ParseException {
        if (s == null || s.isBlank()) return null;
        return new SimpleDateFormat(PATTERN).parse(s);
    }

    public static String format(Date d) {
        if (d == null) return "";
        return new SimpleDateFormat(PATTERN).format(d);
    }

    // === AJOUTS NECESSAIRES ===

    // Ajoute X jours à une date
    public static Date plusJours(Date d, int jours) {
        if (d == null) return null;
        return new Date(d.getTime() + (long) jours * 24 * 60 * 60 * 1000);
    }

    // Calcule le retard en jours (0 si pas de retard)
    public static long joursDeRetard(Date retourReel, Date retourPrevu) {
        if (retourReel == null || retourPrevu == null) return 0;
        long diff = retourReel.getTime() - retourPrevu.getTime();
        long jours = diff / (24L * 60 * 60 * 1000);
        return Math.max(jours, 0); // jamais négatif
    }
}
