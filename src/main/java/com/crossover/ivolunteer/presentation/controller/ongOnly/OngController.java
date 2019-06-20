package com.crossover.ivolunteer.presentation.controller.ongOnly;

import com.crossover.ivolunteer.business.entity.*;
import com.crossover.ivolunteer.business.enums.TipoUsuarioEnum;
import com.crossover.ivolunteer.business.service.EnderecoService;
import com.crossover.ivolunteer.business.service.EventoService;
import com.crossover.ivolunteer.business.service.OngService;
import com.crossover.ivolunteer.business.service.UsuarioService;
import com.crossover.ivolunteer.presentation.constants.ApiPaths;
import com.crossover.ivolunteer.presentation.dto.EventoDto;
import com.crossover.ivolunteer.presentation.dto.NovaOngDto;
import com.crossover.ivolunteer.presentation.dto.RespostaSimplesDto;
import com.crossover.ivolunteer.security.jwt.JWTHttpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Endpoints pra manipular a ONG autenticada.
 */
@RestController
public class OngController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private OngService ongService;

    @Autowired
    private EventoService eventoService;

    @Autowired
    private EnderecoService enderecoService;

    @Autowired
    private JWTHttpService jwtHttpService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PutMapping(ApiPaths.V1.ONG_PREFIX)
    private NovaOngDto update(@Valid @RequestBody NovaOngDto novaOngDto, HttpServletRequest request) {
        Usuario usuario = getAuthenticatedUsuarioOng(request);
        Ong antigaOng = usuario.getOng();
        Endereco antigoEndereco = antigaOng.getEndereco();

        Endereco endereco = null;
        if (novaOngDto.getEndereco() != null) {
            endereco = Endereco.builder()
                    .id(antigoEndereco == null ? null : antigoEndereco.getId())
                    .uf(novaOngDto.getEndereco().getUf())
                    .cidade(novaOngDto.getEndereco().getCidade())
                    .cep(novaOngDto.getEndereco().getCep())
                    .bairro(novaOngDto.getEndereco().getBairro())
                    .complemento1(novaOngDto.getEndereco().getComplemento1())
                    .complemento2(novaOngDto.getEndereco().getComplemento2())
                    .build();
            endereco = enderecoService.save(endereco);
        }

        Ong ong = Ong.builder()
                .id(antigaOng.getId())
                .nome(novaOngDto.getNome())
                .email(novaOngDto.getEmail())
                .areas(novaOngDto.getAreas())
                .dataCriacao(LocalDateTime.now())
                .dataFundacao(novaOngDto.getDataFundacao())
                .endereco(endereco)
                .descricao(novaOngDto.getDescricao())
                .doacoes(novaOngDto.getDoacoes())
                .telefone(novaOngDto.getTelefone())
                .urlFacebook(novaOngDto.getUrlFacebook())
                .urlWebsite(novaOngDto.getUrlWebsite())
                .build();
        ong = ongService.save(ong);

        if (novaOngDto.getUsername() != null)
            usuario.setUsername(novaOngDto.getUsername());
        if (novaOngDto.getSenha() != null)
            usuario.setSenha(passwordEncoder.encode(novaOngDto.getSenha()));
        usuario = usuarioService.save(usuario);

        ong.setUsuario(usuario);
        ong = ongService.save(ong);

        NovaOngDto ongCriada = new NovaOngDto(ong);
        ongCriada.setSenha(novaOngDto.getSenha());
        return ongCriada;
    }

    @PostMapping(ApiPaths.V1.ONG_PREFIX + "/eventos")
    private EventoDto addEvento(@Valid @RequestBody EventoDto eventoDto, HttpServletRequest request) {
        Usuario usuario = getAuthenticatedUsuarioOng(request);
        Ong ong = usuario.getOng();

        if (eventoDto.getId() != null)
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "New Event shouldn't have ID");

        if (eventoDto.getIdOng() != null) {
            if (!Objects.equals(eventoDto.getIdOng(), ong.getId())) {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "New Event's Ong ID should be null or match the authenticated user's Ong ID");
            } else {
                eventoDto.setIdOng(ong.getId());
            }
        } else {
            eventoDto.setIdOng(ong.getId());
        }

        Endereco endereco = Endereco.builder()
                .id(null)
                .uf(eventoDto.getLocal().getUf())
                .cep(eventoDto.getLocal().getCep())
                .cidade(eventoDto.getLocal().getCidade())
                .bairro(eventoDto.getLocal().getBairro())
                .complemento1(eventoDto.getLocal().getComplemento1())
                .complemento2(eventoDto.getLocal().getComplemento2())
                .build();
        endereco = enderecoService.save(endereco);

        Evento evento = Evento.builder()
                .id(null)
                .ong(ong)
                .nome(eventoDto.getNome())
                .descricao(eventoDto.getDescricao())
                .local(endereco)
                .dataCriacao(LocalDateTime.now())
                .dataRealizacao(eventoDto.getDataRealizacao())
                .areas(eventoDto.getAreas())
                .build();
        evento = eventoService.save(evento);

        ong.getEventos().add(evento);
        ong = ongService.save(ong);
        return new EventoDto(evento);
    }

    @PutMapping(ApiPaths.V1.ONG_PREFIX + "/eventos/{id}")
    private EventoDto updateEvento(@Valid @RequestBody EventoDto eventoDto,
                                   @PathVariable("id") Long id,
                                   HttpServletRequest request) {

        Usuario usuario = getAuthenticatedUsuarioOng(request);
        Ong ong = usuario.getOng();

        if (eventoDto.getIdOng() == null) {
            eventoDto.setIdOng(id);
        } else if (!Objects.equals(eventoDto.getId(), id))
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Event's ID should match the path's ID");

        if (eventoDto.getIdOng() != null) {
            if (!Objects.equals(eventoDto.getIdOng(), ong.getId())) {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "New Event's Ong ID should be null or match the authenticated user's Ong ID");
            } else {
                eventoDto.setIdOng(ong.getId());
            }
        } else {
            eventoDto.setIdOng(ong.getId());
        }

        Evento antigoEvento = eventoService.findById(id);
        if (antigoEvento == null)
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Couldn't find Event with specified ID");
        if (!Objects.equals(antigoEvento.getOng().getId(), ong.getId()))
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Specified Event doesn't belong to the authenticated user's Ong");

        Endereco endereco = Endereco.builder()
                .id(null)
                .uf(eventoDto.getLocal().getUf())
                .cep(eventoDto.getLocal().getCep())
                .cidade(eventoDto.getLocal().getCidade())
                .bairro(eventoDto.getLocal().getBairro())
                .complemento1(eventoDto.getLocal().getComplemento1())
                .complemento2(eventoDto.getLocal().getComplemento2())
                .build();
        endereco = enderecoService.save(endereco);

        Evento evento = Evento.builder()
                .id(id)
                .ong(ong)
                .nome(eventoDto.getNome())
                .descricao(eventoDto.getDescricao())
                .local(endereco)
                .dataCriacao(LocalDateTime.now())
                .dataRealizacao(eventoDto.getDataRealizacao())
                .areas(eventoDto.getAreas())
                .build();
        evento = eventoService.save(evento);

        ong.getEventos().add(evento);
        ong = ongService.save(ong);
        return new EventoDto(evento);
    }

    @DeleteMapping(ApiPaths.V1.ONG_PREFIX + "/eventos/{id}")
    private RespostaSimplesDto deleteEvento(@PathVariable("id") Long id,
                                            HttpServletRequest request) {

        Usuario usuario = getAuthenticatedUsuarioOng(request);
        Ong ong = usuario.getOng();

        Evento antigoEvento = eventoService.findById(id);
        if (antigoEvento == null)
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Couldn't find Event with specified ID");
        if (!Objects.equals(antigoEvento.getOng().getId(), ong.getId()))
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Specified Event doesn't belong to the authenticated user's Ong");

        eventoService.deleteById(id);
        return new RespostaSimplesDto(HttpStatus.NO_CONTENT, "Event deleted");
    }

    private Usuario getAuthenticatedUsuarioOng(HttpServletRequest request) throws HttpClientErrorException {
        Sessao sessao = jwtHttpService.getSessaoFromRequest(request);
        Usuario usuario = sessao == null ? null : sessao.getUsuario();
        if (usuario == null)
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        if (usuario.getTipo() != TipoUsuarioEnum.ONG)
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "User isn't ong");
        return usuario;
    }
}
