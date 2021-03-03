package pl.marcin.ppmtool.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProjectidException extends RuntimeException{
    public ProjectidException(String message) {
        super(message);
    }
}
