package com.upgrad.FoodOrderingApp.api.exception;


import com.upgrad.FoodOrderingApp.api.model.ErrorResponse;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RestExceptionHandler {

    /**
     * Exception handler for SignUpRestrictedException
     *
     * @param exe     SignUpRestrictedException type object containing error code and error message
     * @param request The web request object gives access to all the request parameters
     * @return ResponseEntity<ErrorResponse> type object displaying error code and error message along with HttpStatus BAD_REQUEST
     */
    @ExceptionHandler(SignUpRestrictedException.class)
    public ResponseEntity<ErrorResponse> signUpRestrictedException(SignUpRestrictedException exe, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()), HttpStatus.CONFLICT
        );
    }

    /**
     * Exception handler for AuthenticationFailedException
     *
     * @param exe     AuthenticationFailedException type object containing error code and error message
     * @param request The web request object gives access to all the request parameters
     * @return ResponseEntity<ErrorResponse> type object displaying error code and error message along with HttpStatus UNAUTHORIZED
     */
    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ErrorResponse> authenticationFailedException(AuthenticationFailedException exe, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()), HttpStatus.UNAUTHORIZED
        );
    }

    /**
     * Exception handler for AuthorizationFailedException
     *
     * @param exe     AuthorizationFailedException type object containing error code and error message
     * @param request The web request object gives access to all the request parameters
     * @return ResponseEntity<ErrorResponse> type object displaying error code and error message along with HttpStatus FORBIDDEN
     */
    @ExceptionHandler(AuthorizationFailedException.class)
    public ResponseEntity<ErrorResponse> authorizationFailedException(AuthorizationFailedException exe, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()), HttpStatus.FORBIDDEN
        );
    }

    /**
     * Exception handler for UpdateCustomerException
     *
     * @param exe     UpdateCustomerException type object containing error code and error message
     * @param request The web request object gives access to all the request parameters
     * @return ResponseEntity<ErrorResponse> type object displaying error code and error message along with HttpStatus BAD_REQUEST
     */
    @ExceptionHandler(UpdateCustomerException.class)
    public ResponseEntity<ErrorResponse> updateCustomerException(UpdateCustomerException exe, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()), HttpStatus.BAD_REQUEST
        );
    }

    /**
     * Exception handler for CouponNotFoundException
     *
     * @param exe     CouponNotFoundException type object containing error code and error message
     * @param request The web request object gives access to all the request parameters
     * @return ResponseEntity<ErrorResponse> type object displaying error code and error message along with HttpStatus NOT_FOUND
     */
    @ExceptionHandler(CouponNotFoundException.class)
    public ResponseEntity<ErrorResponse> couponNotFoundException(CouponNotFoundException exe, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()), HttpStatus.NOT_FOUND
        );
    }

}