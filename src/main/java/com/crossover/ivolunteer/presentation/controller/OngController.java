package com.crossover.ivolunteer.presentation.controller;

import com.crossover.ivolunteer.business.entity.Endereco;
import com.crossover.ivolunteer.business.entity.Ong;
import com.crossover.ivolunteer.business.entity.Sessao;
import com.crossover.ivolunteer.business.entity.Usuario;
import com.crossover.ivolunteer.business.enums.TipoUsuarioEnum;
import com.crossover.ivolunteer.business.service.EnderecoService;
import com.crossover.ivolunteer.business.service.OngService;
import com.crossover.ivolunteer.business.service.UsuarioService;
import com.crossover.ivolunteer.business.service.VoluntarioService;
import com.crossover.ivolunteer.presentation.constants.ApiPaths;
import com.crossover.ivolunteer.presentation.dto.NovaOngDto;
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
 * Endpoints pra manipular a ong autenticada.
 */
@RestController
public class OngController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private VoluntarioService voluntarioService;

    @Autowired
    private OngService ongService;

    @Autowired
    private EnderecoService enderecoService;

    @Autowired
    private JWTHttpService jwtHttpService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PutMapping(ApiPaths.V1.ONG_PREFIX + "/update")
    private UsuarioDto updateOng(@Valid @RequestBody NovaOngDto novaOngDto, HttpServletRequest request) {

        Sessao sessao = jwtHttpService.getSessaoFromRequest(request);
        Usuario usuario = sessao == null ? null : sessao.getUsuario();
        if (usuario == null)
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);

        if (usuario.getTipo() != TipoUsuarioEnum.ONG)
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "User isn't ong");

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

        usuario = Usuario.builder()
                .id(usuario.getId())
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
