package com.crossover.ivolunteer.security.jwt;

import com.crossover.ivolunteer.business.entity.Sessao;
import com.crossover.ivolunteer.business.service.SessaoService;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filtro que faz todas as requisições HTTP checarem a existência de JWTs (em seus cabeçalhos).
 * Se um JWT válido for encontrado, atualiza o contexto de autenticação.
 */
@Log4j2
public class JWTAuthenticationCheckingFilter extends BasicAuthenticationFilter {

    private JWTHttpService jwtHttpService;

    private SessaoService sessaoService;

    public JWTAuthenticationCheckingFilter(AuthenticationManager authenticationManager,
                                           JWTHttpService jwtHttpService,
                                           SessaoService sessaoService) {
        super(authenticationManager);
        this.jwtHttpService = jwtHttpService;
        this.sessaoService = sessaoService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        Sessao sessao = jwtHttpService.getSessaoFromRequest(request);
        if (sessao != null) {
            Authentication auth = sessaoService.getAuthentication(sessao);
            if (auth != null) {
                SecurityContextHolder.getContext().setAuthentication(auth);
                sessaoService.refresh(sessao);
            }
        }
        chain.doFilter(request, response);
    }
}