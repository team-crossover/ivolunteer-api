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
import java.time.LocalDateTime;
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
        Voluntario voluntario = Voluntario.builder()
                .id(antigoVoluntario.getId())
                .nome(novoVoluntarioDto.getNome())
                .email(novoVoluntarioDto.getEmail())
                .areasInteressadas(novoVoluntarioDto.getAreasInteressadas())
                .dataCriacao(LocalDateTime.now())
                .dataNascimento(novoVoluntarioDto.getDataNascimento())
                .build();
        voluntario = voluntarioService.save(voluntario);

        if (novoVoluntarioDto.getUsername() != null)
            usuario.setUsername(novoVoluntarioDto.getUsername());
        if (novoVoluntarioDto.getSenha() != null)
            usuario.setSenha(passwordEncoder.encode(novoVoluntarioDto.getSenha()));
        usuario = usuarioService.save(usuario);

        voluntario.setUsuario(usuario);
        voluntario = voluntarioService.save(voluntario);

        NovoVoluntarioDto voluntCriado = new NovoVoluntarioDto(voluntario);
        voluntCriado.setSenha(novoVoluntarioDto.getSenha());
        return voluntCriado;
    }

    @PostMapping(ApiPaths.V1.VOLUNTARIO_PREFIX + "/eventos/{idEvento}/confirmar")
    private EventoDto confirmarInteresse(@Valid @RequestBody Boolean deveConfirmar,
                                         @PathVariable Long idEvento,
                                         HttpServletRequest request) {

        Usuario usuario = getAuthenticatedUsuarioVoluntario(request);

        Evento evento = eventoService.findById(idEvento);
        if (evento == null)
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Couldn't find Event with the given ID");

        Voluntario voluntario = usuario.getVoluntario();

        List<Voluntario> confirmados = evento.getConfirmados();
        confirmados.removeIf(v -> Objects.equals(v.getId(), voluntario.getId()));
        if (deveConfirmar)
            confirmados.add(voluntario);
        evento.setConfirmados(confirmados);
        evento = eventoService.save(evento);
        return new EventoDto(evento);
    }

    @PostMapping(ApiPaths.V1.VOLUNTARIO_PREFIX + "/ongs/{idOng}/seguir")
    private OngDto seguirOng(@Valid @RequestBody Boolean deveSeguir,
                             @PathVariable Long idOng,
                             HttpServletRequest request) {

        Usuario usuario = getAuthenticatedUsuarioVoluntario(request);

        Ong ong = ongService.findById(idOng);
        if (ong == null)
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Couldn't find Ong with the given ID");

        Voluntario voluntario = usuario.getVoluntario();

        List<Ong> seguidas = voluntario.getOngsSeguidas();
        seguidas.removeIf(o -> Objects.equals(o.getId(), idOng));
        if (deveSeguir)
            seguidas.add(ong);
        voluntario.setOngsSeguidas(seguidas);
        voluntario = voluntarioService.save(voluntario);
        return new OngDto(ong);
    }

    private Usuario getAuthenticatedUsuarioVoluntario(HttpServletRequest request) throws HttpClientErrorException {
        Sessao sessao = jwtHttpService.getSessaoFromRequest(request);
        if (sessao == null) {
            System.out.println("tem sessao");
        }
        Usuario usuario = sessao == null ? null : sessao.getUsuario();
        if (usuario == null) {
            System.out.println("n ta logado uaaaaaaaaaai? " + request.getHeader("Authorization"));
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        }
        if (usuario.getTipo() != TipoUsuarioEnum.VOLUNTARIO)
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "User isn't voluntario");
        return usuario;
    }
}
