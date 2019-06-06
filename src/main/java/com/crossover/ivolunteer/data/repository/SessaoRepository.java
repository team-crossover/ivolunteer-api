package com.crossover.ivolunteer.data.repository;

import com.crossover.ivolunteer.business.entity.Sessao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface SessaoRepository extends JpaRepository<Sessao, String> {

    long deleteAllByIdNotIn(Collection<String> idSessions);

    void deleteAllByUsuarioId(Long idUsuario);

    long countAllByUsuarioId(Long idUsuario);

    long deleteByTimestampExpiracaoLessThan(Long time);

    Page<Sessao> findAllByUsuarioIdOrderByTimestampExpiracaoDesc(Long idUsuario, Pageable pageable);

}