package com.crossover.ivolunteer.presentation.controller.voluntarioOnly;

import com.crossover.ivolunteer.business.entity.*;
import com.crossover.ivolunteer.business.enums.TipoUsuarioEnum;
import com.crossover.ivolunteer.business.service.EventoService;
import com.crossover.ivolunteer.business.service.OngService;
import com.crossover.ivolunteer.business.service.UsuarioService;
import com.crossover.ivolunteer.business.service.VoluntarioService;
import com.crossover.ivolunteer.presentation.constants.ApiPaths;
import com.crossover.ivolunteer.presentation.dto.EventoDto;
import com.crossover.ivolunteer.presentation.dto.NovoVoluntarioDto;
import com.crossover.ivolunteer.presentation.dto.OngDto;
import com.crossover.ivolunteer.security.jwt.JWTHttpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * Endpoints pra manipular o voluntario autenticado.
 */
@RestController
public class VoluntarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private VoluntarioService voluntarioService;

    @Autowired
    private OngService ongService;

    @Autowired
    private EventoService eventoService;

    @Autowired
    private JWTHttpService jwtHttpService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PutMapping(ApiPaths.V1.VOLUNTARIO_PREFIX)
    private NovoVoluntarioDto update(@Valid @RequestBody NovoVoluntarioDto novoVoluntarioDto,
                                     HttpServletRequest request) {
        Usuario usuario = getAuthenticatedUsuarioVoluntario(request);

        Voluntario antigoVoluntario = usuario.getVoluntario();
        novoVoluntarioDto.setId(antigoVoluntario.getId());
        Voluntario voluntario = novoVoluntarioDto.toVoluntario(voluntarioService);

        if (novoVoluntarioDto.getUsername() != null)
            usuario.setUsername(novoVoluntarioDto.getUsername());
        if (novoVoluntarioDto.getSenha() != null)
            usuario.setSenha(passwordEncoder.encode(novoVoluntarioDto.getSenha()));
        usuario = usuarioService.save(usuario);

        voluntario.setUsuario(usuario);
        voluntario = voluntarioService.save(voluntario);

        return new NovoVoluntarioDto(voluntario, novoVoluntarioDto.getSenha());
    }

    @PostMapping(ApiPaths.V1.VOLUNTARIO_PREFIX + "/eventos/{idEvento}/confirmar")
    private EventoDto confirmarInteresse(@RequestParam(name = "valor", required = true) Boolean valor,
                                         @PathVariable Long idEvento,
                                         HttpServletRequest request) {

        Usuario usuario = getAuthenticatedUsuarioVoluntario(request);

        Evento evento = eventoService.findById(idEvento);
        if (evento == null)
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Couldn't find Event with the given ID");

        Voluntario voluntario = usuario.getVoluntario();

        List<Voluntario> confirmados = evento.getConfirmados();
        confirmados.removeIf(v -> Objects.equals(v.getId(), voluntario.getId()));
        if (valor)
            confirmados.add(voluntario);
        evento.setConfirmados(confirmados);
        evento = eventoService.save(evento);
        return new EventoDto(evento);
    }

    @PostMapping(ApiPaths.V1.VOLUNTARIO_PREFIX + "/ongs/{idOng}/seguir")
    private OngDto seguirOng(@RequestParam(name = "valor", required = true) Boolean valor,
                             @PathVariable Long idOng,
                             HttpServletRequest request) {

        Usuario usuario = getAuthenticatedUsuarioVoluntario(request);

        Ong ong = ongService.findById(idOng);
        if (ong == null)
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Couldn't find Ong with the given ID");

        Voluntario voluntario = usuario.getVoluntario();

        List<Ong> seguidas = voluntario.getOngsSeguidas();
        seguidas.removeIf(o -> Objects.equals(o.getId(), idOng));
        if (valor)
            seguidas.add(ong);
        voluntario.setOngsSeguidas(seguidas);
        voluntario = voluntarioService.save(voluntario);
        return new OngDto(ong);
    }

    private Usuario getAuthenticatedUsuarioVoluntario(HttpServletRequest request) throws HttpClientErrorException {
        Sessao sessao = jwtHttpService.getSessaoFromRequest(request);
        Usuario usuario = sessao == null ? null : sessao.getUsuario();
        if (usuario == null) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        }
        if (usuario.getTipo() != TipoUsuarioEnum.VOLUNTARIO)
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "User isn't voluntario");
        return usuario;
    }
}
