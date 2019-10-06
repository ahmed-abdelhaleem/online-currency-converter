package com.currency.converter.errorhandling;

import org.springframework.http.HttpStatus;

public class ProcessingException extends Exception{

    /**
	 * 
	 */
	private static final long serialVersionUID = -9102085414140512363L;
	private String errorCode;
    private HttpStatus httpStatus;

    public ProcessingException(String errorCode,String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
    }
    public ProcessingException(String errorCode,String errorMessage,Throwable ex) {
        super(errorMessage);
        this.errorCode = errorCode;
        initCause(ex);
    }
    public ProcessingException(String errorCode,String errorMessage, HttpStatus httpStatus) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public ProcessingException(String errorCode,String errorMessage, HttpStatus httpStatus,Throwable ex) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        initCause(ex);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}