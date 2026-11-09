package cl.duoc.api_eventos.controllers;

import cl.duoc.api_eventos.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    // Inyectamos únicamente lo necesario: el AuthenticationManager y el JwtUtil
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> createAuthenticationToken(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (Exception e) {
            throw new Exception("Credenciales incorrectas", e);
        }

        final String jwt = jwtUtil.generateToken(authRequest.getUsername());

        return ResponseEntity.ok(new AuthResponse(jwt));
    }

    // DTOs internos para la petición y respuesta de autenticación
    public static class AuthRequest {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class AuthResponse {
        private final String jwt;

        public AuthResponse(String jwt) {
            this.jwt = jwt;
        }

        public String getJwt() { return jwt; }
    }
}