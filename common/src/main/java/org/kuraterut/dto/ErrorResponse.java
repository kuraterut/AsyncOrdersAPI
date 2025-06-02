package org.kuraterut.dto;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    public ErrorResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(HttpStatusCode status, String message) {
        this.status = HttpStatus.valueOf(status.value());
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    private HttpStatus status;
    private String message;
    private LocalDateTime timestamp;
}
