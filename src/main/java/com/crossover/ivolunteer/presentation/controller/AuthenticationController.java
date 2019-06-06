package com.crossover.ivolunteer.presentation.controller;

import com.crossover.ivolunteer.business.entity.Sessao;
import com.crossover.ivolunteer.business.entity.Usuario;
import com.crossover.ivolunteer.business.service.SessaoService;
import com.crossover.ivolunteer.business.service.UsuarioService;
import com.crossover.ivolunteer.presentation.constants.ApiPaths;
import com.crossover.ivolunteer.presentation.dto.RespostaSimplesDto;
import com.crossover.ivolunteer.presentation.dto.UsuarioDto;
import com.crossover.ivolunteer.security.jwt.JWTHttpService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Log4j2
@RestController
public class AuthenticationController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private SessaoService sessaoService;

    @Autowired
    private JWTHttpService jwtHttpService;

    // NOTE: Authentication is handled at WebSecurityConfig.

    @GetMapping(ApiPaths.V1.AUTH_WHOAMI)
    public UsuarioDto whoami(HttpServletRequest request) {
        // Fetches the user from the request's session.
        Sessao sessao = jwtHttpService.getSessaoFromRequest(request);
        Usuario usuario = sessao == null ? null : sessao.getUsuario();
        if (usuario == null)
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        return new UsuarioDto(usuario);
    }

    /**
     * Desautentica a sessão atual (com base no JWT da requisição).
     *
     * @param all Se presente e verdadeiro, todas as sessões do mesmo usuáro serão desautenticadas.
     */
    @GetMapping(ApiPaths.V1.AUTH_DEAUTHENTICATE)
    public RespostaSimplesDto deauthenticate(@RequestParam(name = "all", required = false) Boolean all,
                                             HttpServletRequest request) {

        // Fetches the usuario from the request's sessao.
        Sessao sessao = jwtHttpService.getSessaoFromRequest(request);
        if (sessao == null)
            return new RespostaSimplesDto(
                    HttpStatus.UNAUTHORIZED,
                    ApiPaths.V1.AUTH_DEAUTHENTICATE,
                    "No valid token was provided");

        // Clears current authentication and invalidates current HTTP sessao
        SecurityContextHolder.getContext().setAuthentication(null);
        HttpSession httpSession = request.getSession(false);
        if (httpSession != null)
            httpSession.invalidate();

        Usuario usuario = sessao.getUsuario();
        if (all != null && all) {
            sessaoService.deleteAllByUsuarioId(usuario.getId());
            return new RespostaSimplesDto(
                    HttpStatus.OK,
                    ApiPaths.V1.AUTH_DEAUTHENTICATE,
                    "Deauthenticated all of the user's session");
        } else {
            sessaoService.deleteById(sessao.getId());
            return new RespostaSimplesDto(
                    HttpStatus.OK,
                    ApiPaths.V1.AUTH_DEAUTHENTICATE,
                    "Deauthenticated the token's session");
        }
    }

}