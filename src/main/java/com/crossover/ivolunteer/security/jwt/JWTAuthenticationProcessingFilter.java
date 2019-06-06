package com.crossover.ivolunteer.security.jwt;

import com.crossover.ivolunteer.business.entity.Sessao;
import com.crossover.ivolunteer.business.entity.Usuario;
import com.crossover.ivolunteer.business.service.SessaoService;
import com.crossover.ivolunteer.business.service.UsuarioService;
import com.crossover.ivolunteer.presentation.dto.CredenciaisDto;
import com.crossover.ivolunteer.presentation.dto.RespostaSimplesDto;
import com.crossover.ivolunteer.security.exception.InvalidAuthenticationRequestException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * Filtro lida com o processamento de autenticação via requisições HTTP com JWTs.
 */
@Log4j2
public class JWTAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private JWTHttpService jwtHttpService;

    private UsuarioService usuarioService;

    private SessaoService sessaoService;

    public JWTAuthenticationProcessingFilter(RequestMatcher matcher,
                                             AuthenticationManager authenticationManager,
                                             JWTHttpService jwtHttpService,
                                             UsuarioService usuarioService,
                                             SessaoService sessaoService) {
        super(matcher);
        this.jwtHttpService = jwtHttpService;
        this.usuarioService = usuarioService;
        this.sessaoService = sessaoService;
        setAuthenticationManager(authenticationManager);
    }

    /**
     * Lida com o pedido de autenticação via HTTP.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        // Fetch the request data
        CredenciaisDto credenciaisDto = fetchCredenciais(request);

        // Create an authentication token for the given auth request
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(
                        credenciaisDto.getUsername(),
                        credenciaisDto.getSenha());
        if (authenticationDetailsSource != null)
            token.setDetails(authenticationDetailsSource.buildDetails(request));

        // Authenticate with the created token
        return getAuthenticationManager().authenticate(token);
    }

    /**
     * Quando a autenticação ocorre com sucesso, retorna uma reposta de OK.
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain filterChain,
                                            Authentication auth) throws IOException {
        // Create a new session for the user
        String username = auth.getName();
        Usuario user = usuarioService.findByUsername(username);
        Sessao session = sessaoService.createForUsuario(user);

        // Add a JWT with the created session to the response
        jwtHttpService.addSessaoToResponse(session, response);

        // Announces the success
        String path = request.getServletPath();
        RespostaSimplesDto result = new RespostaSimplesDto(HttpStatus.OK, path);
        result.setMessage("Authentication successful");
        response.setStatus(HttpStatus.OK.value());
        new ObjectMapper().writeValue(response.getWriter(), result);
    }

    private CredenciaisDto fetchCredenciais(HttpServletRequest request) throws InvalidAuthenticationRequestException {
        try {
            InputStream inputStream = request.getInputStream();
            return new ObjectMapper().readValue(inputStream, CredenciaisDto.class);
        } catch (IOException e) {
            throw new InvalidAuthenticationRequestException(e.getMessage());
        }
    }
}
