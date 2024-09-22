package hampusborg.webservicesproject.constant;

public final class Constant {
    public static final String PHOTO_DIRECTORY = System.getProperty("user.home") + "/Downloads/uploads/";

    private Constant() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}