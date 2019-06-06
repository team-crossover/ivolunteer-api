package com.crossover.ivolunteer.data.config;

import com.crossover.ivolunteer.business.entity.Endereco;
import com.crossover.ivolunteer.business.entity.Ong;
import com.crossover.ivolunteer.business.entity.Usuario;
import com.crossover.ivolunteer.business.entity.Voluntario;
import com.crossover.ivolunteer.business.enums.AreaEnum;
import com.crossover.ivolunteer.business.enums.TipoUsuarioEnum;
import com.crossover.ivolunteer.business.service.EnderecoService;
import com.crossover.ivolunteer.business.service.OngService;
import com.crossover.ivolunteer.business.service.UsuarioService;
import com.crossover.ivolunteer.business.service.VoluntarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * Adds initial data to the database when the system is initialized.
 */
@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

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

    private boolean alreadySetup = false;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!alreadySetup) {
            createInitialAdmins();
            createInitialVoluntarios();
            createInitialOngs();
//            createInitialEventos();
//            createInitialPostagens();
            alreadySetup = true;
        }
    }

    private void createInitialAdmins() {
        Usuario usuario = new Usuario();
        usuario.setUsername("admin");
        usuario.setSenha(passwordEncoder.encode("123"));
        usuario.setTipo(TipoUsuarioEnum.ADMIN);
        usuario = usuarioService.save(usuario);
    }

    private void createInitialVoluntarios() {
        Usuario usuario = new Usuario();
        usuario.setUsername("voluntario");
        usuario.setSenha(passwordEncoder.encode("123"));
        usuario = usuarioService.save(usuario);

        Voluntario voluntario = new Voluntario();
        voluntario.setNome("Bette Davis");
        voluntario.setAreasInteressadas(Arrays.asList(AreaEnum.CULTURA_E_ARTE, AreaEnum.DIREITOS_HUMANOS));
        voluntario.setDataCriacao(LocalDateTime.now());
        voluntario.setDataNascimento(LocalDate.of(1908, 4, 5));
        voluntario.setEmail("bettinha@hollywood.com");
        voluntario.setUsuario(usuario);
        voluntario = voluntarioService.save(voluntario);

        usuario.setTipo(TipoUsuarioEnum.VOLUNTARIO);
        usuario.setVoluntario(voluntario);
        usuario = usuarioService.save(usuario);
    }

    private void createInitialOngs() {
        Usuario usuario = new Usuario();
        usuario.setUsername("ong");
        usuario.setSenha(passwordEncoder.encode("123"));
        usuario = usuarioService.save(usuario);

        Endereco endereco = new Endereco();
        endereco.setUf("GO");
        endereco.setCidade("Goiânia");
        endereco.setCep("74303303");
        endereco.setBairro("Setor Sudoeste");
        endereco.setComplemento1("Rua Bolinha Azul, Lote 48");
        endereco = enderecoService.save(endereco);

        Ong ong = new Ong();
        ong.setNome("Clube do Gato");
        ong.setDescricao("Cuidamos de gatos perdidos, sem vida, sem rumo, possuídos etc. Não cuidamos de cachorros pois somos malignos.");
        ong.setAreas(Arrays.asList(AreaEnum.ANIMAIS, AreaEnum.MEIO_AMBIENTE));
        ong.setDataCriacao(LocalDateTime.now());
        ong.setDataFundacao(LocalDate.of(2005, 8, 6));
        ong.setEmail("contato@clubedogato.com");
        ong.setUrlFacebook("www.facebook.com/clubedogato");
        ong.setUrlWebsite("www.clubedogato.com");
        ong.setTelefone("(62) 3030-3030");
        ong.setEndereco(endereco);
        ong.setUsuario(usuario);
        ong = ongService.save(ong);

        usuario.setTipo(TipoUsuarioEnum.ONG);
        usuario.setOng(ong);
        usuario = usuarioService.save(usuario);
    }

}
