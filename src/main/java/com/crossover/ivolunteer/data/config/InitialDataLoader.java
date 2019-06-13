package com.crossover.ivolunteer.data.config;

import com.crossover.ivolunteer.business.entity.*;
import com.crossover.ivolunteer.business.enums.TipoUsuarioEnum;
import com.crossover.ivolunteer.business.service.*;
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
    private EventoService eventoService;

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
            createInitialAdmin();
            createInitialVoluntarios();
            createInitialOngs();
            alreadySetup = true;
        }
    }

    private void createInitialAdmin() {
        Usuario usuario = new Usuario();
        usuario.setUsername("admin");
        usuario.setSenha(passwordEncoder.encode("123"));
        usuario.setTipo(TipoUsuarioEnum.ADMIN);
        usuario = usuarioService.save(usuario);
    }

    private void createInitialVoluntarios() {
        Voluntario voluntario = new Voluntario();
        voluntario.setNome("Bette Davis");
        voluntario.setAreasInteressadas(Arrays.asList("Cultura e arte", "Direitos humanos"));
        voluntario.setDataNascimento(LocalDate.of(1908, 4, 5));
        voluntario.setEmail("bettinha@hollywood.com");
        saveInitialVoluntario("voluntario", voluntario);

        Voluntario voluntario2 = new Voluntario();
        voluntario2.setNome("Kátia Peuri");
        voluntario2.setAreasInteressadas(Arrays.asList("Cultura e arte", "Animais", "Educação"));
        voluntario2.setDataNascimento(LocalDate.of(1980, 1, 1));
        voluntario2.setEmail("nro@katyperry.com");
        saveInitialVoluntario("voluntario2", voluntario2);
    }

    private void createInitialOngs() {
        // ONG 1

        Endereco endereco1 = new Endereco();
        endereco1.setUf("GO");
        endereco1.setCidade("Goiânia");
        endereco1.setCep("74303303");
        endereco1.setBairro("Setor Sudoeste");
        endereco1.setComplemento1("Rua Bolinha Azul, Lote 48");
        Ong ong1 = new Ong();
        ong1.setNome("Clube do Gato");
        ong1.setDescricao("Cuidamos de gatos perdidos, sem vida, sem rumo, possuídos etc. Não cuidamos de cachorros pois somos malignos.");
        ong1.setAreas(Arrays.asList("Animais", "Meio ambiente"));
        ong1.setDataFundacao(LocalDate.of(2005, 8, 6));
        ong1.setEmail("contato@clubedogato.com");
        ong1.setUrlFacebook("www.facebook.com/clubedogato");
        ong1.setUrlWebsite("www.clubedogato.com");
        ong1.setTelefone("(62) 3030-3030");
        Evento evento1 = new Evento();
        evento1.setNome("Castração de gatos");
        evento1.setDescricao("Remoção de testículos gratuita, apenas para gatos e gatas");
        evento1.setAreas(Arrays.asList("Animais"));
        evento1.setDataRealizacao(LocalDateTime.of(2019, 1, 5, 18, 30));
        Evento evento2 = new Evento();
        evento2.setNome("Adoção de gatos");
        evento2.setDescricao("Feira de adoção de gatos resgatados da rua, todos vacinados e castrados");
        evento2.setAreas(Arrays.asList("Animais"));
        evento2.setDataRealizacao(LocalDateTime.of(2019, 6, 12, 22, 30));
        saveInitialOng("ong", endereco1, ong1, evento1, evento2);

        // ONG 2

        Endereco endereco2 = new Endereco();
        endereco2.setUf("GO");
        endereco2.setCidade("Goiânia");
        endereco2.setCep("74303370");
        endereco2.setBairro("Setor Billboard");
        endereco2.setComplemento1("Rua Never Really Over, Lote 15");
        Ong ong2 = new Ong();
        ong2.setNome("Amigos da Música");
        ong2.setDescricao("Empenhados em compartilhar música com todos que precisam. Aulas gratuitas, shows gratuitos e mutirões de streaming.");
        ong2.setAreas(Arrays.asList("Cultura e arte", "Educação"));
        ong2.setDataFundacao(LocalDate.of(2011, 1, 2));
        ong2.setEmail("amigos@damusica.com");
        ong2.setUrlFacebook("www.facebook.com/amigosdamusica");
        ong2.setUrlWebsite("www.amigosdamusica.com");
        ong2.setTelefone("(62) 12345-1234");
        Evento evento3 = new Evento();
        evento3.setNome("Roda de samba");
        evento3.setDescricao("Vai ter rodas e vai ter sambas");
        evento3.setAreas(Arrays.asList("Cultura e arte"));
        evento3.setDataRealizacao(LocalDateTime.of(2019, 10, 5, 18, 30));
        saveInitialOng("ong2", endereco2, ong2, evento3);
    }

    private void saveInitialOng(String username, Endereco endereco, Ong ong, Evento... eventos) {
        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setSenha(passwordEncoder.encode("123"));
        usuario = usuarioService.save(usuario);

        endereco = enderecoService.save(endereco);

        ong.setDataCriacao(LocalDateTime.now());
        ong.setEndereco(endereco);
        ong.setUsuario(usuario);
        ong = ongService.save(ong);

        usuario.setTipo(TipoUsuarioEnum.ONG);
        usuario.setOng(ong);
        usuario = usuarioService.save(usuario);

        for (Evento evento : eventos) {
            evento.setOng(ong);
            evento.setLocal(ong.getEndereco());
            evento.setDataCriacao(LocalDateTime.now());
            evento = eventoService.save(evento);
        }
    }

    private void saveInitialVoluntario(String username, Voluntario voluntario) {
        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setSenha(passwordEncoder.encode("123"));
        usuario = usuarioService.save(usuario);

        voluntario.setDataCriacao(LocalDateTime.now());
        voluntario.setUsuario(usuario);
        voluntario = voluntarioService.save(voluntario);

        usuario.setTipo(TipoUsuarioEnum.VOLUNTARIO);
        usuario.setVoluntario(voluntario);
        usuario = usuarioService.save(usuario);
    }
}
