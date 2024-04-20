package co.com.hotel.message;

public enum TechnicalExceptionMessage {
    COULD_NOT_REGISTER_USER("HIT001", "Could no create user, please check the request"),
    COULD_NOT_REGISTER_ROOM("HIT002", "Could no create room, please check the request");


    private final String code;
    private final String message;


    TechnicalExceptionMessage(String code, String message) {
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
