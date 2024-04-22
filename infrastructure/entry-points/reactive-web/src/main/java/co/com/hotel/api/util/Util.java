package co.com.hotel.api.util;

import co.com.hotel.TechnicalException;
import co.com.hotel.api.exception.BusinessException;
import co.com.hotel.api.exception.model.ErrorModel;
import co.com.hotel.api.exception.model.ErrorResponse;
import co.com.hotel.message.TechnicalExceptionMessage;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;
import java.util.Objects;

@Component
public class Util {

    private ErrorModel errorModel;

    public ErrorResponse buildErrorResponse(TechnicalException technicalException,
                                            String domain) {
        errorModel = new ErrorModel();
        errorModel.setCode(technicalException.getTechnicalExceptionMessage().getCode());
        errorModel.setMessage(technicalException.getMessage());
        errorModel.setReason(technicalException.getMessage());
        errorModel.setDomain(domain);

        var errorResponse = new ErrorResponse(List.of(errorModel));
        return errorResponse;
    }

    public ErrorResponse buildErrorResponse(BusinessException businessException,
                                            String domain) {
        errorModel = new ErrorModel();
        errorModel.setCode(businessException.getBusinessExceptionMessage().getCode());
        errorModel.setMessage(businessException.getMessage());
        errorModel.setReason(businessException.getMessage());
        errorModel.setDomain(domain);

        var errorResponse = new ErrorResponse(List.of(errorModel));
        return errorResponse;
    }

    public ErrorResponse buildErrorResponse(String domain) {

        errorModel = new ErrorModel();
        errorModel.setCode(TechnicalExceptionMessage.UN_EXPECTED_ERROR.getCode());
        errorModel.setMessage(TechnicalExceptionMessage.UN_EXPECTED_ERROR.getMessage());
        errorModel.setReason(TechnicalExceptionMessage.UN_EXPECTED_ERROR.getMessage());
        errorModel.setDomain(domain);

        var errorResponse = new co.com.hotel.api.exception.model.ErrorResponse(List.of(errorModel));
        return errorResponse;
    }

    public String buildDomain(ServerHttpRequest request) {
        return request.getMethod().name().concat(":").concat(request.getURI().getPath());
    }

    public Object validateNull(Object object) {
        return Objects.requireNonNullElse(object, "");
    }
}
