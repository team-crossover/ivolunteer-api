package com.crossover.ivolunteer.business.service;

import com.crossover.ivolunteer.business.entity.Sessao;
import com.crossover.ivolunteer.business.entity.Usuario;
import com.crossover.ivolunteer.data.repository.SessaoRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Log4j2
@Service
@Transactional
public class SessaoService extends EntityServiceBase<Sessao, String, SessaoRepository> {

    private static final long EXPIRATION_TIME_MS = 1000 * 60 * 60 * 24 * 7; // 7 days

    private static final int MAX_SESSIONS_PER_USER = 10;

    @Autowired
    private UsuarioService usuarioService;

    public boolean isOwnedByUsuario(String idSessao, Long idUsuario) {
        Usuario usuario = usuarioService.findById(idUsuario);
        if (usuario != null)
            for (Sessao s : usuario.getSessoes())
                if (s.getId().equals(idSessao))
                    return true;
        return false;
    }

    public Authentication getAuthentication(Sessao sessao) {
        if (System.currentTimeMillis() > sessao.getTimestampExpiracao()) {
            log.debug("Invalid session: expired");
            return null;
        }
        Usuario user = sessao.getUsuario();
        if (user == null) {
            log.debug("Invalid session: has no user");
            return null;
        }
        return new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                user.getSenha(),
                user.getTipo().getAuthorities());
    }

    @Transactional(readOnly = false)
    public Sessao createForUsuario(Usuario user) {
        long now = System.currentTimeMillis();

        // Add the new session
        Sessao session = Sessao.builder()
                .usuario(user)
                .timestampCriacao(now)
                .timestampExpiracao(now + EXPIRATION_TIME_MS)
                .build();
        save(session);

        // Kills sessions above the per-user limit (older expiration timestamp die first)
        if (repository.countAllByUsuarioId(user.getId()) > MAX_SESSIONS_PER_USER) {
            Page<Sessao> sessionsToKeep =
                    repository.findAllByUsuarioIdOrderByTimestampExpiracaoDesc(
                            user.getId(), PageRequest.of(0, MAX_SESSIONS_PER_USER));
            List<String> sessionsToKeepIds =
                    sessionsToKeep.stream().map(Sessao::getId).collect(Collectors.toList());
            repository.deleteAllByIdNotIn(sessionsToKeepIds);
        }

        return session;
    }

    @Transactional(readOnly = false)
    public void refresh(Sessao session) {
        long now = System.currentTimeMillis();
        session.setTimestampExpiracao(now + EXPIRATION_TIME_MS);
        save(session);
    }

    @Transactional(readOnly = false)
    public void deleteAllByUsuarioId(Long userId) {
        repository.deleteAllByUsuarioId(userId);
    }

    @Transactional(readOnly = false)
    public void deleteExpired() {
        Long now = System.currentTimeMillis();
        long count = repository.deleteByTimestampExpiracaoLessThan(now);
        log.info("Deleted " + count + " expired sessions.");
    }

}
