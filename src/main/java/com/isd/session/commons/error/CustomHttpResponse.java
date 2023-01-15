package com.isd.session.commons.error;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class CustomHttpResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private HttpStatus code;

    private String message;

    private String trace;

    public CustomHttpResponse(HttpStatus code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

}
