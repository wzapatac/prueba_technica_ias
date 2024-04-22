package co.com.hotel.api.exception;

import co.com.hotel.api.exception.message.BusinessExceptionMessage;

public class BusinessException extends RuntimeException {

    private final BusinessExceptionMessage businessExceptionMessage;


    public BusinessException(BusinessExceptionMessage businessExceptionMessage) {
        super(businessExceptionMessage.getMessage());
        this.businessExceptionMessage = businessExceptionMessage;
    }

    public BusinessExceptionMessage getBusinessExceptionMessage() {
        return businessExceptionMessage;
    }
}
