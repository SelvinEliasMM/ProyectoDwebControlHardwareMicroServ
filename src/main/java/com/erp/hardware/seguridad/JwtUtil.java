package com.erp.hardware.seguridad;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    // clave de 256 bits para firma hmac-sha256
    private static final String SECRET_STRING = "TechFixERP_ClaveSecretaUltraSegura_2026_HardwareDB_Token!";
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_STRING.getBytes(StandardCharsets.UTF_8));

    // tiempo de expiración del token 24 horas en milisegundos
    private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000L;

    public String generarToken(Integer idUsuario, String correo, String nombreCompleto, String rol) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("idUsuario", idUsuario);
        claims.put("nombreCompleto", nombreCompleto);
        claims.put("rol", rol);

        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .subject(correo)
                .claims(claims)
                .issuedAt(ahora)
                .expiration(expiracion)
                .signWith(key)
                .compact();
    }

    public Claims obtenerClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean esTokenValido(String token) {
        try {
            Claims claims = obtenerClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public String obtenerCorreo(String token) {
        return obtenerClaims(token).getSubject();
    }

    public String obtenerRol(String token) {
        return (String) obtenerClaims(token).get("rol");
    }

    public Integer obtenerIdUsuario(String token) {
        Number id = (Number) obtenerClaims(token).get("idUsuario");
        return id != null ? id.intValue() : null;
    }
}
