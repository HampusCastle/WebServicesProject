package hampusborg.webservicesproject.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @GetMapping("/auth/check")
    public ResponseEntity<Void> checkAuthentication(Authentication authentication) {
        return ResponseEntity.ok().build();
    }
}