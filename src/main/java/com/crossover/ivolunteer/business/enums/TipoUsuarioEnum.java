package com.crossover.ivolunteer.business.enums;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.List;

public enum TipoUsuarioEnum {
    ADMIN,
    VOLUNTARIO,
    ONG;

    public List<GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_" + this.name()));
    }
}
