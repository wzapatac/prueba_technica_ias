package co.com.hotel;

import co.com.hotel.message.TechnicalExceptionMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TechnicalExceptionTest {

    @Test
    void validateTechnicalException(){
        var message = TechnicalExceptionMessage.UN_EXPECTED_ERROR;
        var technicalException = new TechnicalException(message);

        Assertions.assertNotNull(technicalException);
        Assertions.assertEquals("HIT005", technicalException.getTechnicalExceptionMessage().getCode());
    }

}