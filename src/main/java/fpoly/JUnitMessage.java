package fpoly;

public class JUnitMessage {
    private String message;

    public JUnitMessage(String message) {
        this.message = message;
    }

    // Method gây lỗi chia cho 0
    public void printMessage() {
        System.out.println(message);
        int divide = 1 / 0; // ArithmeticException
    }

    // Method bình thường
    public String printHiMessage() {
        message = "Hi " + message;
        System.out.println(message);
        return message;
    }
}
