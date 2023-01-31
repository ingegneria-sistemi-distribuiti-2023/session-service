package com.isd.session.commons.error;

import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * Custom Exception used to handle error on Request
 */
@Data
@NoArgsConstructor
public class CustomServiceException extends Exception {

    private static final long serialVersionUID = 1L;

    private CustomHttpResponse httpErrorMessage;

    public CustomServiceException(CustomHttpResponse httpErrorMessage) {
        this(httpErrorMessage.getMessage());
        this.httpErrorMessage = httpErrorMessage;
    }
    public CustomServiceException(String message) {
        super(message);
    }

}
