package com.crossover.ivolunteer.presentation.controller;

import com.crossover.ivolunteer.business.entity.Sessao;
import com.crossover.ivolunteer.business.entity.Usuario;
import com.crossover.ivolunteer.business.entity.Voluntario;
import com.crossover.ivolunteer.business.enums.TipoUsuarioEnum;
import com.crossover.ivolunteer.business.service.UsuarioService;
import com.crossover.ivolunteer.business.service.VoluntarioService;
import com.crossover.ivolunteer.presentation.constants.ApiPaths;
import com.crossover.ivolunteer.presentation.dto.NovoVoluntarioDto;
import com.crossover.ivolunteer.presentation.dto.UsuarioDto;
import com.crossover.ivolunteer.security.jwt.JWTHttpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;

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
    private JWTHttpService jwtHttpService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PutMapping(ApiPaths.V1.VOLUNTARIO_PREFIX + "/update")
    private UsuarioDto updateVoluntario(@Valid @RequestBody NovoVoluntarioDto novoVoluntarioDto, HttpServletRequest request) {

        Sessao sessao = jwtHttpService.getSessaoFromRequest(request);
        Usuario usuario = sessao == null ? null : sessao.getUsuario();
        if (usuario == null)
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);

        if (usuario.getTipo() != TipoUsuarioEnum.VOLUNTARIO)
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "User isn't voluntáro");

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

        usuario = Usuario.builder()
                .id(usuario.getId())
                .username(novoVoluntarioDto.getUsername())
                .senha(passwordEncoder.encode(novoVoluntarioDto.getSenha()))
                .tipo(TipoUsuarioEnum.VOLUNTARIO)
                .voluntario(voluntario)
                .build();
        usuario = usuarioService.save(usuario);

        voluntario.setUsuario(usuario);
        voluntario = voluntarioService.save(voluntario);
        return new UsuarioDto(usuario);
    }

}
