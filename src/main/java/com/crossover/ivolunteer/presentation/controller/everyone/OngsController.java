package com.crossover.ivolunteer.presentation.controller.everyone;

import com.crossover.ivolunteer.business.entity.Imagem;
import com.crossover.ivolunteer.business.entity.Ong;
import com.crossover.ivolunteer.business.entity.Usuario;
import com.crossover.ivolunteer.business.service.EnderecoService;
import com.crossover.ivolunteer.business.service.ImagemService;
import com.crossover.ivolunteer.business.service.OngService;
import com.crossover.ivolunteer.business.service.UsuarioService;
import com.crossover.ivolunteer.presentation.constants.ApiPaths;
import com.crossover.ivolunteer.presentation.dto.NovaOngDto;
import com.crossover.ivolunteer.presentation.dto.OngDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
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
    private ImagemService imagemService;

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
        return ongService.getAllDtoFiltered(nome, areas);
    }

    @PostMapping(ApiPaths.V1.ONGS_PREFIX)
    public NovaOngDto addOng(@RequestBody @Valid NovaOngDto novaOngDto) {
        Usuario usuario = usuarioService.findByUsername(novaOngDto.getUsername());
        if (usuario != null)
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "Username already exists");

        // Salva a imagem caso veio com imagem.
        if (novaOngDto.getSrcImgPerfil() != null) {
            Imagem img = Imagem.builder().src(novaOngDto.getSrcImgPerfil()).build();
            img = imagemService.save(img);
            novaOngDto.setIdImgPerfil(img.getId());
        }

        Ong ong = novaOngDto.toOng(ongService, enderecoService, imagemService);
        usuario = novaOngDto.toUsuario(ong, passwordEncoder, usuarioService, ongService);
        return new NovaOngDto(ong, novaOngDto.getSenha());
    }
}
