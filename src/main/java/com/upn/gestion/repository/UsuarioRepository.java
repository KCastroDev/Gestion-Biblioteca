package com.upn.gestion.repository;

import com.upn.gestion.model.UsuarioSistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioSistema, Long> {
    // Método  para buscar usuario por email
    Optional<UsuarioSistema> findByEmail(String email);
    Optional<UsuarioSistema> findByDni(String dni);
    boolean existsByDni(String dni);
    boolean existsByEmail(String email);
}