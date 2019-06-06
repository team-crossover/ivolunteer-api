package com.crossover.ivolunteer.security.jwt;

import com.crossover.ivolunteer.business.entity.Sessao;
import com.crossover.ivolunteer.business.entity.Usuario;
import com.crossover.ivolunteer.business.service.SessaoService;
import com.crossover.ivolunteer.business.service.UsuarioService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Representa um JSON Web Token assinalado (signed), usado para autenticação via requisições HTTP.
 */
@Log4j2
public class JWT {

    // TODO: Adicionar encriptação

    @Getter
    private final String idSessao;

    @Getter
    private final String username;

    @Getter
    private final String senha;

    @Getter
    private final Date dataExpiracao;

    private JWT(Date dataExpiracao, String username, String senha, String idSessao) {
        this.dataExpiracao = dataExpiracao;
        this.username = username;
        this.senha = senha;
        this.idSessao = idSessao;
    }

    public static JWT fromSessao(Sessao sessao) {
        return new JWT(
                new Date(sessao.getTimestampExpiracao()),
                sessao.getUsuario().getUsername(),
                sessao.getUsuario().getSenha(),
                sessao.getId());
    }

    public static JWT fromTokenString(String tokenString, SecretKey secretSigningKey) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretSigningKey)
                    .parseClaimsJws(tokenString)
                    .getBody();
            return new JWT(
                    claims.getExpiration(),
                    claims.getSubject(),
                    (String) claims.get("senha"),
                    (String) claims.get("session-id"));
        } catch (JwtException ex) {
            return null;
        }
    }

    /**
     * Tenta obter a Sessão correspondente a este JWT.
     * Retorna nulo caso o JWT seja inválido.
     */
    public Sessao fetchSession(UsuarioService usuarioService,
                               SessaoService sessaoService) {
        if (username == null) {
            log.debug("Invalid JWT: username is null");
            return null;
        }
        Usuario usuario = usuarioService.findByUsername(username);
        if (usuario == null) {
            log.debug("Invalid JWT: username doesn't exist or was changed");
            return null;
        } else if (!senha.equals(usuario.getSenha())) {
            log.debug("Invalid JWT: senha was changed");
            return null;
        } else if (!sessaoService.isOwnedByUsuario(idSessao, usuario.getId())) {
            log.debug("Invalid JWT: user doesn't own this session ID (maybe it was removed)");
            return null;
        }
        return sessaoService.findById(idSessao);
    }

    /**
     * Converts the JWT to its signed token representation.
     */
    public String toTokenString(SecretKey secretKey) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(dataExpiracao)
                .claim("senha", senha)
                .claim("session-id", idSessao)
                .signWith(secretKey)
                .compact();
    }
}
