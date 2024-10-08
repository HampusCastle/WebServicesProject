package hampusborg.webservicesproject.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class ErrorResponse {

    @Getter
    @Setter
    private String errorType;
    private String message;
    private int statusCode;

    public ErrorResponse(String errorType, String message, int statusCode) {
        this.errorType = errorType;
        this.message = message;
        this.statusCode = statusCode;
    }
}