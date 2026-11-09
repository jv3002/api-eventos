package cl.duoc.api_eventos.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    // Configuración de la cadena de seguridad HTTP
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF ya que usamos tokens JWT sin estado
            .authorizeHttpRequests(auth -> auth
                // Rutas públicas (por ejemplo: autenticación, eventos públicos, H2 Console si se usa)
                .requestMatchers("/api/auth/**", "/api/eventos/**", "/h2-console/**").permitAll()
                // Todas las demás rutas (como compras, estadísticas, modificaciones) requerirán autenticación
                .anyRequest().authenticated()
            )
            // Permitir marcos para la consola H2 si estuviera activa
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
            // Gestión de sesión sin estado (Stateless) para REST APIs con JWT
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // Agregar nuestro filtro JWT antes del filtro de autenticación por usuario/contraseña
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Bean para gestionar los usuarios en memoria (requerido por JwtRequestFilter y AuthenticationManager)
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .roles("ADMIN")
                .build();

        UserDetails cliente = User.builder()
                .username("cliente")
                .password(passwordEncoder.encode("cliente123"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, cliente);
    }

    // Bean para encriptar contraseñas con BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean para el administrador de autenticación
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}