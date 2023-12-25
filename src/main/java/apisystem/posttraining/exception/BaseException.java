package apisystem.posttraining.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseException extends RuntimeException {
    private HttpStatus code;
    private String message;
}
