package com.crossover.ivolunteer.presentation.controller;

import com.crossover.ivolunteer.business.entity.Endereco;
import com.crossover.ivolunteer.business.entity.Ong;
import com.crossover.ivolunteer.business.entity.Usuario;
import com.crossover.ivolunteer.business.entity.Voluntario;
import com.crossover.ivolunteer.business.enums.TipoUsuarioEnum;
import com.crossover.ivolunteer.business.service.EnderecoService;
import com.crossover.ivolunteer.business.service.OngService;
import com.crossover.ivolunteer.business.service.UsuarioService;
import com.crossover.ivolunteer.business.service.VoluntarioService;
import com.crossover.ivolunteer.presentation.constants.ApiPaths;
import com.crossover.ivolunteer.presentation.dto.NovaOngDto;
import com.crossover.ivolunteer.presentation.dto.NovoVoluntarioDto;
import com.crossover.ivolunteer.presentation.dto.UsuarioDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
public class UserController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private VoluntarioService voluntarioService;

    @Autowired
    private OngService ongService;

    @Autowired
    private EnderecoService enderecoService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(ApiPaths.V1.USERS_PREFIX + "/create/voluntario")
    private UsuarioDto createVoluntario(@Valid @RequestBody NovoVoluntarioDto novoVoluntarioDto) {

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
        return new UsuarioDto(usuario);
    }

    @PostMapping(ApiPaths.V1.USERS_PREFIX + "/create/ong")
    private UsuarioDto createOng(@Valid @RequestBody NovaOngDto novaOngDto) {

        Usuario usuario = usuarioService.findByUsername(novaOngDto.getUsername());
        if (usuario != null)
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "Username already exists");

        Endereco endereco = null;
        if (novaOngDto.getEndereco() != null) {
            endereco = Endereco.builder()
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
                .nome(novaOngDto.getNome())
                .email(novaOngDto.getEmail())
                .areas(novaOngDto.getAreas())
                .dataCriacao(LocalDateTime.now())
                .dataFundacao(novaOngDto.getDataFundacao())
                .endereco(endereco)
                .descricao(novaOngDto.getDescricao())
                .doacoes(novaOngDto.getDoacao())
                .telefone(novaOngDto.getTelefone())
                .urlFacebook(novaOngDto.getUrlFacebook())
                .urlWebsite(novaOngDto.getUrlWebsite())
                .build();
        ong = ongService.save(ong);

        usuario = Usuario.builder()
                .username(novaOngDto.getUsername())
                .senha(passwordEncoder.encode(novaOngDto.getSenha()))
                .tipo(TipoUsuarioEnum.ONG)
                .ong(ong)
                .build();
        usuario = usuarioService.save(usuario);

        ong.setUsuario(usuario);
        ong = ongService.save(ong);
        return new UsuarioDto(usuario);
    }
    
}
