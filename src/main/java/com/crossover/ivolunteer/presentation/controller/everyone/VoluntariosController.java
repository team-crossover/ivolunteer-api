package com.crossover.ivolunteer.presentation.controller.everyone;

import com.crossover.ivolunteer.business.entity.Usuario;
import com.crossover.ivolunteer.business.entity.Voluntario;
import com.crossover.ivolunteer.business.enums.TipoUsuarioEnum;
import com.crossover.ivolunteer.business.service.UsuarioService;
import com.crossover.ivolunteer.business.service.VoluntarioService;
import com.crossover.ivolunteer.presentation.constants.ApiPaths;
import com.crossover.ivolunteer.presentation.dto.NovoVoluntarioDto;
import com.crossover.ivolunteer.presentation.dto.VoluntarioDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
public class VoluntariosController {

    @Autowired
    private VoluntarioService voluntarioService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping(ApiPaths.V1.VOLUNTARIOS_PREFIX + "/{id}")
    private VoluntarioDto get(@PathVariable("id") long id) {
        Voluntario voluntario = voluntarioService.findById(id);
        if (voluntario == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Voluntario not found");
        return new VoluntarioDto(voluntario);
    }

    @GetMapping(ApiPaths.V1.VOLUNTARIOS_PREFIX)
    private Collection<VoluntarioDto> getAll() {
        // TODO: Add pagination to this
        return voluntarioService.findAll().stream().map(VoluntarioDto::new).collect(Collectors.toList());
    }

    @PostMapping(ApiPaths.V1.VOLUNTARIOS_PREFIX)
    private NovoVoluntarioDto add(@Valid @RequestBody NovoVoluntarioDto novoVoluntarioDto) {

        Usuario usuario = usuarioService.findByUsername(novoVoluntarioDto.getUsername());
        if (usuario != null)
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "Username already exists");

        Voluntario voluntario = Voluntario.builder()
                .nome(novoVoluntarioDto.getNome())
                .email(novoVoluntarioDto.getEmail())
                .areasInteressadas(novoVoluntarioDto.getAreasInteressadas())
                .dataCriacao(LocalDateTime.now())
                .dataNascimento(novoVoluntarioDto.getDataNascimento())
                .build();
        voluntario = voluntarioService.save(voluntario);

        usuario = Usuario.builder()
                .username(novoVoluntarioDto.getUsername())
                .senha(passwordEncoder.encode(novoVoluntarioDto.getSenha()))
                .tipo(TipoUsuarioEnum.VOLUNTARIO)
                .voluntario(voluntario)
                .build();
        usuario = usuarioService.save(usuario);

        voluntario.setUsuario(usuario);
        voluntario = voluntarioService.save(voluntario);

        NovoVoluntarioDto voluntCriado = new NovoVoluntarioDto(voluntario);
        voluntCriado.setSenha(novoVoluntarioDto.getSenha());
        return voluntCriado;
    }

}
