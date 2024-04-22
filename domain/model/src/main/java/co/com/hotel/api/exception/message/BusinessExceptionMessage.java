package co.com.hotel.api.exception.message;

public enum BusinessExceptionMessage {

    UN_EXPECTED_ERROR("HIB0001", "Un expected error was presented"),
    IDENTIFICATION_NULL("HIB0002", "User identification is a required field to do the reservation"),
    NAME_NULL("HIB0003", "User name is a required field to do the reservation"),
    START_DATE_NULL("HIB0004", "Start date is a required field to do the reservation"),
    END_DATE_NULL("HIB0005", "End date is a required field to do the reservation"),
    ROOM_NULL("HIB0006", "Number of room is a required field to do the reservation"),
    ROOM_NOT_EXIST("HIB0007", "The room requested don´t exist, please validate the available rooms"),
    ROOM_NOT_AVAILABLE("HIB0008", "The room requested don´t is available, please validate the available rooms"),
    IDENTIFICATION_LENGTH("HIB0009", "The identification must to contain less or equal of 12 characters"),
    NAME_LENGTH("HIB0009", "The name must to contain less or equal of 30 characters"),
    NAME_NUMBER("HIB0010", "The name can´t contain numbers");



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
