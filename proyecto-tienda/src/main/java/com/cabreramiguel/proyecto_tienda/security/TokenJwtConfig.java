package com.cabreramiguel.proyecto_tienda.security;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class TokenJwtConfig {
    
    // public static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();
    // Clave fija para que los tokens sean válidos entre reinicios
    public static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor("X9f!aPz7qL@3vRk#2sTnYw$8bHcD^5eG".getBytes());
    public static final String PREFIX_TOKEN = "Bearer ";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String CONTENT_TYPE = "application/json";
}
