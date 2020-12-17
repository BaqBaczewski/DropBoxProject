package example.audiohive.app.badge;

public class Badge {

    private final String label;
    private final String color;

    public Badge(String label, String color) {
        this.label = label;
        this.color = color;
    }

    public String getLabel() {
        return label;
    }

    public String getColor() {
        return color;
    }
}
