package fpoly.junit;

public class JUnitMessage {
    private final String message;

    public JUnitMessage(String message) {
        this.message = message;
    }

    public void printMessage() {
        System.out.println(message);
    }

    public String printHiMessage() {
        return "Hi " + message;
    }
}
