package shop.helloshop.web.exception;

public class ItemException extends IllegalStateException{
    public ItemException() {
        super();
    }

    public ItemException(String s) {
        super(s);
    }

    public ItemException(String message, Throwable cause) {
        super(message, cause);
    }

    public ItemException(Throwable cause) {
        super(cause);
    }
}
