package com.whoz_in.api.shared.response;

import com.whoz_in.domain.shared.BusinessException;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@UtilityClass
public class ResponseEntityGenerator {

    public static <D> ResponseEntity<SuccessBody<D>> success(D data, String message, HttpStatus status) {
        return new ResponseEntity<>(
                new SuccessBody<>(data, message), status);
    }

    public static <D> ResponseEntity<SuccessBody<D>> success(D data, ApiResponseCode responseCode){
        return success(data, responseCode.getMessage(), responseCode.getHttpStatus());
    }

    public static ResponseEntity<SuccessBody<Void>> success(String message, HttpStatus status) {
        return success(null, message, status);
    }

    public static ResponseEntity<SuccessBody<Void>> success(ApiResponseCode responseCode){
        return success(responseCode.getMessage(), responseCode.getHttpStatus());
    }


    public static ResponseEntity<FailureBody> fail(String code, String message, HttpStatus status) {
        return new ResponseEntity<>(
                new FailureBody(code, message), status);
    }

    public static ResponseEntity<FailureBody> fail(BusinessException businessException, HttpStatus status){
        return fail(businessException.getErrorCode(), businessException.getErrorMessage(), status);
    }
}
