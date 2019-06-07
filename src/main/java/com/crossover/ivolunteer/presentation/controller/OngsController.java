package com.crossover.ivolunteer.presentation.controller;

import com.crossover.ivolunteer.business.entity.Endereco;
import com.crossover.ivolunteer.business.entity.Ong;
import com.crossover.ivolunteer.business.entity.Usuario;
import com.crossover.ivolunteer.business.enums.TipoUsuarioEnum;
import com.crossover.ivolunteer.business.service.EnderecoService;
import com.crossover.ivolunteer.business.service.OngService;
import com.crossover.ivolunteer.business.service.UsuarioService;
import com.crossover.ivolunteer.presentation.constants.ApiPaths;
import com.crossover.ivolunteer.presentation.dto.NovaOngDto;
import com.crossover.ivolunteer.presentation.dto.UsuarioDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collection;

@RestController
public class OngsController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private OngService ongService;

    @Autowired
    private EnderecoService enderecoService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(ApiPaths.V1.ONGS_PREFIX)
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
                .doacoes(novaOngDto.getDoacoes())
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

    // TODO: OngDto
    @GetMapping(ApiPaths.V1.ONGS_PREFIX)
    private Collection<Ong> getAll() {
        // TODO: Add pagination to this
        return ongService.findAll();
    }

    // TODO: OngDto
    @GetMapping(ApiPaths.V1.ONGS_PREFIX + "/{id}")
    private Ong get(@PathVariable("id") long id) {
        Ong ong = ongService.findById(id);
        if (ong == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return ong;
    }

}
