public class AnsiColor {
    // Store the 3 bit color (values 0 through 7)
    final private int color;

    static public String[] names = {"black", "red", "green", "yellow", "blue", "magenta", "cyan", "white"};

    public AnsiColor(String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("Color input cannot be null or empty");
        }

        int color = findIndex(names, input.toLowerCase());

        if (color == -1) {
             throw new IllegalArgumentException("Color must be one of: " + String.join(", ", names));
        }

        this.color = color;
    }

    // Return -1 if the target is not found
    public static int findIndex(String[] array, String target) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(target)) return i;
        }
        return -1; 
    }

    public int getColorValue() { return color; }

    @Override
    public String toString() { return names[color]; }

    private String getBackgroundColor() {
        return "\u001B[4" + color + "m";
    }

    private static final String ANSI_RESET = "\u001B[0m";

    public void printBlock() {
        System.out.print(getBackgroundColor() + "  " + ANSI_RESET);
    }
}