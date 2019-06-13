package com.crossover.ivolunteer.presentation.controller.everyone;

import com.crossover.ivolunteer.business.entity.Endereco;
import com.crossover.ivolunteer.business.entity.Ong;
import com.crossover.ivolunteer.business.entity.Usuario;
import com.crossover.ivolunteer.business.enums.TipoUsuarioEnum;
import com.crossover.ivolunteer.business.service.EnderecoService;
import com.crossover.ivolunteer.business.service.OngService;
import com.crossover.ivolunteer.business.service.UsuarioService;
import com.crossover.ivolunteer.presentation.constants.ApiPaths;
import com.crossover.ivolunteer.presentation.dto.NovaOngDto;
import com.crossover.ivolunteer.presentation.dto.OngDto;
import com.crossover.ivolunteer.util.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @GetMapping(ApiPaths.V1.ONGS_PREFIX + "/{id}")
    private OngDto get(@PathVariable("id") long id) {
        Ong ong = ongService.findById(id);
        if (ong == null)
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        return new OngDto(ong);
    }

    static class OngComparatorBySeguidores implements Comparator<Ong> {
        public int compare(Ong c1, Ong c2) {
            return c1.getSeguidores().size() - c2.getSeguidores().size();
        }
    }

    @GetMapping(ApiPaths.V1.ONGS_PREFIX)
    private Collection<OngDto> getAll(@RequestParam(name = "nome", required = false) String nome,
                                      @RequestParam(name = "areas", required = false) String[] areas) {
        // TODO: Add pagination to this
        Stream<Ong> ongs = ongService.findAll().stream();
        if (nome != null && nome.length() > 0) {
            String finalNome = nome.toLowerCase();
            ongs = ongs.filter(o -> o.getNome().toLowerCase().contains(finalNome));
        }
        if (areas != null && areas.length > 0) {
            ongs = ongs.filter(o -> ArrayUtils.containsAny(o.getAreas().toArray(), areas));
        }
        ongs = ongs.sorted(new OngComparatorBySeguidores());
        return ongs.map(OngDto::new).collect(Collectors.toList());
    }

    @PostMapping(ApiPaths.V1.ONGS_PREFIX)
    private NovaOngDto add(@Valid @RequestBody NovaOngDto novaOngDto) {

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

        NovaOngDto ongCriada = new NovaOngDto(ong);
        ongCriada.setSenha(novaOngDto.getSenha());
        return ongCriada;
    }
}
