package com.crossover.ivolunteer.business.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "enderecos")
public class Endereco {

    @Id
    @GeneratedValue
    private Long id;

    @Size(min = 2, max = 2)
    @Column(nullable = false)
    private String uf;

    @Size(max = 50)
    @Column(nullable = false)
    private String cidade;

    @Size(min = 8, max = 8)
    @Column(nullable = false)
    private String cep;

    @Size(max = 50)
    @Column(nullable = false)
    private String bairro;

    @Size(max = 50)
    private String complemento1;

    @Size(max = 50)
    private String complemento2;

}
