package caribou;

public class CaribouException extends RuntimeException {
    public CaribouException() {
        super();
    }

    public CaribouException(String message) {
        super(message);
    }

    public CaribouException(String message, Throwable cause) {
        super(message, cause);
    }

    public CaribouException(Throwable cause) {
        super(cause);
    }
}