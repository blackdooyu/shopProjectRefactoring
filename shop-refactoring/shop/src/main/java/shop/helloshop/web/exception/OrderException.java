package shop.helloshop.web.exception;

public class OrderException extends IllegalStateException{
    public OrderException() {
    }

    public OrderException(String s) {
        super(s);
    }

    public OrderException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderException(Throwable cause) {
        super(cause);
    }
}
