//package com.gyuzal.whozin.shared.infrastructure.presentation;
//
//import java.util.List;
//import lombok.experimental.UtilityClass;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.FieldError;
//
//@UtilityClass
//public class ResponseEntityGenerator {
//
//    public static ResponseEntity<ApiResponseBody.SuccessBody<Void>> success(
//            final HttpStatus status) {
//        return new ResponseEntity<>(
//                new ApiResponseBody.SuccessBody<>(null, , ), status);
//    }
//
//    public static <D> ResponseEntity<ApiResponseBody.SuccessBody<D>> success(
//            D data, HttpStatus status, ) {
//        return new ResponseEntity<>(
//                new ApiResponseBody.SuccessBody<>(data, code.getMessage(), code.getCode()), status);
//    }
//
//    public static ResponseEntity<ApiResponseBody.FailureBody> fail(
//            String message, String code, HttpStatus status) {
//        return new ResponseEntity<>(
//                new ApiResponseBody.FailureBody(String.valueOf(status.value()), code, message), status);
//    }
//
//    public static ResponseEntity<ApiResponseBody.FailureBody> fail(
//            BindingResult bindingResult, String code, HttpStatus status) {
//        return new ResponseEntity<>(
//                new ApiResponseBody.FailureBody(
//                        String.valueOf(status.value()), code, createErrorMessage(bindingResult)),
//                status);
//    }
//
//    private static String createErrorMessage(BindingResult bindingResult) {
//        StringBuilder sb = new StringBuilder();
//        boolean isFirst = true;
//
//        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
//        for (FieldError fieldError : fieldErrors) {
//            if (!isFirst) {
//                sb.append(", ");
//            } else {
//                isFirst = false;
//            }
//            sb.append("[");
//            sb.append(fieldError.getField());
//            sb.append("] ");
//            sb.append(fieldError.getDefaultMessage());
//        }
//
//        return sb.toString();
//    }
//}
