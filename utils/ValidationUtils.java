package utils;

public final class ValidationUtils {
    private ValidationUtils() {}

    public static void assureEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Email invalide");
        }
    }
}
