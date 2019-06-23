package com.crossover.ivolunteer.presentation.controller.everyone;

import com.crossover.ivolunteer.business.entity.Ong;
import com.crossover.ivolunteer.business.entity.Usuario;
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
import java.util.Collection;
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
        ongs = ongService.sort(ongs);
        return ongs.map(OngDto::new).collect(Collectors.toList());
    }

    @PostMapping(ApiPaths.V1.ONGS_PREFIX)
    private NovaOngDto add(@Valid @RequestBody NovaOngDto novaOngDto) {

        Usuario usuario = usuarioService.findByUsername(novaOngDto.getUsername());
        if (usuario != null)
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "Username already exists");

        Ong ong = novaOngDto.toOng(ongService, enderecoService);
        usuario = novaOngDto.toUsuario(ong, passwordEncoder, usuarioService, ongService);
        return new NovaOngDto(ong, novaOngDto.getSenha());
    }
}
