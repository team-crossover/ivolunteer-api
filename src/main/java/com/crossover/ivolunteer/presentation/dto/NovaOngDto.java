package com.crossover.ivolunteer.presentation.dto;

import com.crossover.ivolunteer.business.entity.Endereco;
import com.crossover.ivolunteer.business.entity.Ong;
import com.crossover.ivolunteer.business.entity.Usuario;
import com.crossover.ivolunteer.business.enums.TipoUsuarioEnum;
import com.crossover.ivolunteer.business.service.EnderecoService;
import com.crossover.ivolunteer.business.service.OngService;
import com.crossover.ivolunteer.business.service.UsuarioService;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NovaOngDto {

    private Long id;

    @NotBlank
    private String username;

    @NotBlank
    private String senha;

    @NotBlank
    private String nome;

    @NotBlank
    private String descricao;

    private String doacoes;

    @JsonFormat(pattern = ("MM/dd/yyyy"))
    private LocalDate dataFundacao;

    private List<String> areas;

    private String telefone;

    private String email;

    private String urlFacebook;

    private String urlWebsite;

    private EnderecoDto endereco;

    private String imgPerfil;

    private List<String> imgsGaleria = new ArrayList<>();

    public NovaOngDto(Ong ong) {
        this.id = ong.getId();
        this.username = ong.getUsuario().getUsername();
        this.senha = null; // Não tem como retornar a senha porque armazenamos apenas a hash.
        this.nome = ong.getNome();
        this.descricao = ong.getDescricao();
        this.doacoes = ong.getDoacoes();
        this.dataFundacao = ong.getDataFundacao();
        this.telefone = ong.getTelefone();
        this.email = ong.getEmail();
        this.urlFacebook = ong.getUrlFacebook();
        this.urlWebsite = ong.getUrlWebsite();
        this.endereco = new EnderecoDto(ong.getEndereco());
        this.imgPerfil = ong.getImgPerfil();
        this.imgsGaleria = ong.getImgsGaleria();
    }

    public NovaOngDto(Ong ong, String senha) {
        this.id = ong.getId();
        this.username = ong.getUsuario().getUsername();
        this.senha = senha;
        this.nome = ong.getNome();
        this.descricao = ong.getDescricao();
        this.doacoes = ong.getDoacoes();
        this.dataFundacao = ong.getDataFundacao();
        this.telefone = ong.getTelefone();
        this.email = ong.getEmail();
        this.urlFacebook = ong.getUrlFacebook();
        this.urlWebsite = ong.getUrlWebsite();
        this.endereco = new EnderecoDto(ong.getEndereco());
        this.imgPerfil = ong.getImgPerfil();
        this.imgsGaleria = ong.getImgsGaleria();
    }

    public Ong toOng(OngService ongService, EnderecoService enderecoService) {
        Endereco endereco = null;
        if (getEndereco() != null) {
            endereco = getEndereco().toEntity();
            endereco = enderecoService.save(endereco);
        }
        Ong ong = Ong.builder()
                .id(id)
                .nome(getNome())
                .email(getEmail())
                .areas(getAreas())
                .dataCriacao(LocalDateTime.now())
                .dataFundacao(getDataFundacao())
                .endereco(endereco)
                .descricao(getDescricao())
                .doacoes(getDoacoes())
                .telefone(getTelefone())
                .urlFacebook(getUrlFacebook())
                .urlWebsite(getUrlWebsite())
                .imgPerfil(getImgPerfil())
                .imgsGaleria(getImgsGaleria())
                .build();
        return ongService.save(ong);
    }

    public Usuario toUsuario(Ong ong, PasswordEncoder passwordEncoder, UsuarioService usuarioService, OngService ongService) {
        Usuario usuario = Usuario.builder()
                .username(getUsername())
                .senha(passwordEncoder.encode(getSenha()))
                .tipo(TipoUsuarioEnum.ONG)
                .ong(ong)
                .build();
        ong.setUsuario(usuario);
        ong = ongService.save(ong);
        return usuarioService.save(usuario);
    }
}
