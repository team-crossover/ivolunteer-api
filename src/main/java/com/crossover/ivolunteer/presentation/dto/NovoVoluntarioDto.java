package com.crossover.ivolunteer.presentation.dto;

import com.crossover.ivolunteer.business.entity.Usuario;
import com.crossover.ivolunteer.business.entity.Voluntario;
import com.crossover.ivolunteer.business.enums.TipoUsuarioEnum;
import com.crossover.ivolunteer.business.service.UsuarioService;
import com.crossover.ivolunteer.business.service.VoluntarioService;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NovoVoluntarioDto {

    private Long id;

    @NotBlank
    private String username;

    private String senha;

    @NotBlank
    private String nome;

    private String email;

    @JsonFormat(pattern = ("MM/dd/yyyy"))
    private LocalDate dataNascimento;

    private List<String> areasInteressadas;

    private String imgPerfil;

    public NovoVoluntarioDto(Voluntario voluntario, String senha) {
        this.id = voluntario.getId();
        this.username = voluntario.getUsuario().getUsername();
        this.senha = senha; // Não tem como recuperar a senha porque armazenamos apenas a hash.
        this.nome = voluntario.getNome();
        this.email = voluntario.getEmail();
        this.dataNascimento = voluntario.getDataNascimento();
        this.areasInteressadas = voluntario.getAreasInteressadas();
        this.imgPerfil = voluntario.getImgPerfil();
    }

    public Voluntario toVoluntario(VoluntarioService voluntarioService) {
        Voluntario voluntario = Voluntario.builder()
                .id(getId())
                .nome(getNome())
                .email(getEmail())
                .areasInteressadas(getAreasInteressadas())
                .dataCriacao(LocalDateTime.now())
                .dataNascimento(getDataNascimento())
                .imgPerfil(getImgPerfil())
                .build();
        return voluntarioService.save(voluntario);
    }

    public Usuario toUsuario(Voluntario voluntario, PasswordEncoder passwordEncoder, UsuarioService usuarioService, VoluntarioService voluntarioService) {
        Usuario usuario = Usuario.builder()
                .username(getUsername())
                .senha(passwordEncoder.encode(getSenha()))
                .tipo(TipoUsuarioEnum.VOLUNTARIO)
                .voluntario(voluntario)
                .build();
        voluntario.setUsuario(usuario);
        voluntario = voluntarioService.save(voluntario);
        return usuarioService.save(usuario);
    }
}
