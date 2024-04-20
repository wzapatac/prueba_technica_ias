package co.com.hotel.exception.message;

public enum BusinessExceptionMessage {

    UN_EXPECTED_ERROR("HIB0001", "Un expected error was presented");

    private final String code;
    private final String message;

    BusinessExceptionMessage(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
