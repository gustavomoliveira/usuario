package com.gustavodeoliveira.usuario.infrastructure.repository;

import com.gustavodeoliveira.usuario.infrastructure.entity.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByEmail(String email); // script do próprio banco de dados

    Optional<Usuario> findByEmail(String email);

    @Transactional // sem erros no momento da deleção
    void deleteByEmail(String email);
}
