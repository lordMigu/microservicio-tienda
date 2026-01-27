package com.cabreramiguel.proyecto_tienda.security.filter;

import static com.cabreramiguel.proyecto_tienda.security.TokenJwtConfig.CONTENT_TYPE;
import static com.cabreramiguel.proyecto_tienda.security.TokenJwtConfig.HEADER_AUTHORIZATION;
import static com.cabreramiguel.proyecto_tienda.security.TokenJwtConfig.PREFIX_TOKEN;
import static com.cabreramiguel.proyecto_tienda.security.TokenJwtConfig.SECRET_KEY;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.cabreramiguel.proyecto_tienda.security.SimpleGrantedAuthorityJsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class JwtValidationFilter extends BasicAuthenticationFilter {

    
    public JwtValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String header = request.getHeader(HEADER_AUTHORIZATION);

        // Si el header no existe o no comienza con el prefijo "Bearer ", 
        // permite que la solicitud continúe sin validar token
        if (header == null || !header.startsWith(PREFIX_TOKEN)) {
            chain.doFilter(request, response);
            return;
        }
        
        // Extrae el token limpiando el prefijo "Bearer "
        String token = header.replace(PREFIX_TOKEN, "");

        try {
            // Valida la firma del token usando la clave secreta y extrae los claims
            Claims claims = Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();
            
            // Extrae el nombre de usuario del claim "subject"
            String usename = claims.getSubject();
            // String usename2 = (String) claims.get("username");
            
            // Extrae las autoridades (roles) del claim "authorities"
            Object authoritiesClaims = claims.get("authorities");

            // Deserializa las autoridades JSON a objetos SimpleGrantedAuthority
            // utilizando un ObjectMapper con un MixIn para la deserialización correcta
            Collection<? extends GrantedAuthority> authorities = Arrays.asList(
                    new ObjectMapper()
                .addMixIn(SimpleGrantedAuthority.class, 
                                    SimpleGrantedAuthorityJsonCreator.class)
                .readValue(authoritiesClaims.toString().getBytes(), SimpleGrantedAuthority[].class)
                );

            // Crea un token de autenticación con el usuario y sus autoridades
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(usename, null, authorities);
            
            // Establece la autenticación en el contexto de seguridad de Spring
            // para que esté disponible en toda la solicitud
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            
            // Continúa con el siguiente filtro en la cadena
            chain.doFilter(request, response);
            
        } catch (JwtException e) {
            // Manejo de errores de validación del JWT
            Map<String, String> body = new HashMap<>();
            body.put("error", e.getMessage());
            body.put("message", "El token JWT es invalido!");

            // Retorna una respuesta JSON con el error
            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            
            // Establece el estado HTTP 401 (No autorizado)
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            
            // Define el tipo de contenido como JSON
            response.setContentType(CONTENT_TYPE);
        }
    }

}