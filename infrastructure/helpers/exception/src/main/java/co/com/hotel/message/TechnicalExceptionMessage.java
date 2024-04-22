package co.com.hotel.message;

public enum TechnicalExceptionMessage {
    COULD_NOT_REGISTER_USER("HIT001", "Could not create user, please check the request"),
    COULD_NOT_REGISTER_ROOM("HIT002", "Could not create room, please check the request"),
    COULD_NOT_LIST_ROOM("HIT003", "Could not list the room, please check the request"),
    COULD_NOT_LIST_ALL_ROOMS("HIT004", "Could not list all rooms, please check the request"),
    UN_EXPECTED_ERROR("HIT005", "Un expected error was presented");


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
