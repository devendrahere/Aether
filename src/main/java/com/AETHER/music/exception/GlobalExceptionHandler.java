import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // existing handlers stay as-is

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDenied(AccessDeniedException e) {
        return ResponseEntity.status(403)
                .body(Map.of("error", "Access denied"));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuth(AuthenticationException e) {
        return ResponseEntity.status(401)
                .body(Map.of("error", "Authentication required"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleFallback(Exception e) {
        // LOG THIS IN REAL APPS
        return ResponseEntity.status(500)
                .body(Map.of(
                        "error", "Internal server error",
                        "type", e.getClass().getSimpleName()
                ));
    }
}
