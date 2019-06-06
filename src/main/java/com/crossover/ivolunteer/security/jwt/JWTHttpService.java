package com.crossover.ivolunteer.security.jwt;

import com.crossover.ivolunteer.business.entity.Sessao;
import com.crossover.ivolunteer.business.service.SessaoService;
import com.crossover.ivolunteer.business.service.UsuarioService;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

@Log4j2
@Service
@Transactional
public class JWTHttpService {

    private static final String HTTP_HEADER = "Authorization";

    private static final String HTTP_PREFIX = "Bearer";

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private SessaoService sessaoService;

    @Value("${security.jwt.signing-secret-key}")
    private String rawSecretSigningKey;

    private SecretKey secretSigninKey;

    @PostConstruct
    public void init() {
        this.secretSigninKey = parseSecretSigningKey();
    }

    public Sessao getSessaoFromRequest(HttpServletRequest request) {
        String token = request.getHeader(HTTP_HEADER);
        if (token == null || !token.startsWith(HTTP_PREFIX))
            return null;

        String tokenString = token.replace(HTTP_PREFIX, "").trim();
        JWT jwt = JWT.fromTokenString(tokenString, secretSigninKey);
        return jwt == null ? null : jwt.fetchSession(usuarioService, sessaoService);
    }

    public void addSessaoToResponse(Sessao sessao, HttpServletResponse response) {
        JWT jwt = JWT.fromSessao(sessao);
        String tokenString = jwt.toTokenString(secretSigninKey);
        response.addHeader(HTTP_HEADER, HTTP_PREFIX + " " + tokenString);
    }

    private SecretKey parseSecretSigningKey() {
        try {
            byte[] bytes = rawSecretSigningKey.getBytes(StandardCharsets.UTF_8);
            return Keys.hmacShaKeyFor(bytes);
        } catch (Exception ex) {
            SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
            log.error("\n\nCouldn't get the signing secret key from properties! " +
                    "Using a generated key: " + new String(key.getEncoded()) + "\n\n", ex);
            return key;
        }
    }

}
