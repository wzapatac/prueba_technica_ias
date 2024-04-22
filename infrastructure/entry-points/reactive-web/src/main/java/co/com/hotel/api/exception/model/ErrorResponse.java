package co.com.hotel.api.exception.model;

import java.util.List;

public class ErrorResponse {
    private List<ErrorModel> errors;

    public ErrorResponse(List<ErrorModel> errors) {
        this.errors = errors;
    }

    public List<ErrorModel> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorModel> errors) {
        this.errors = errors;
    }
}
