package com.gestaocrise.security;

import com.gestaocrise.entity.Usuario;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class JwtTokenProvider {

    private static final long EXPIRATION_TIME = 3600; // 1 hora em segundos

    public String gerarToken(Usuario usuario) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(EXPIRATION_TIME);

        Set<String> roles = new HashSet<>();
        roles.add(usuario.getPerfil().getNome());

        return Jwt.issuer("gestaocrise-api")
                .subject(usuario.getId().toString())
                .expiresAt(expiresAt)
                .claim("email", usuario.getEmail())
                .claim("nome", usuario.getNome())
                .claim("perfil", usuario.getPerfil().getNome())
                .groups(roles)
                .sign();
    }
}
