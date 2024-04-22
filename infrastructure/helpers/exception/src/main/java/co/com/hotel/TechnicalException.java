package co.com.hotel;

import co.com.hotel.message.TechnicalExceptionMessage;

public class TechnicalException extends RuntimeException{
    private final TechnicalExceptionMessage technicalExceptionMessage;


    public TechnicalException(TechnicalExceptionMessage technicalExceptionMessage) {
        super(technicalExceptionMessage.getMessage());
        this.technicalExceptionMessage = technicalExceptionMessage;
    }

    public TechnicalExceptionMessage getTechnicalExceptionMessage() {
        return technicalExceptionMessage;
    }
}
