package com.upn.gestion.service;

import com.upn.gestion.model.UsuarioSistema;
import com.upn.gestion.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Método para validar login
    public UsuarioSistema login(String email, String password) {
        // 1. Buscamos por email
        Optional<UsuarioSistema> usuarioOpt = usuarioRepository.findByEmail(email);

        // 2. Si existe, verificamos la contraseña
        if (usuarioOpt.isPresent()) {
            UsuarioSistema usuario = usuarioOpt.get();
            if (usuario.getPassword().equals(password)) {
                return usuario; // Login exitoso
            }
        }
        return null; // Login fallido
    }

    // Método para guardar/registrar un usuario
    public UsuarioSistema save(UsuarioSistema usuario) {
        return usuarioRepository.save(usuario);
    }

    // Método adicional: Verificar si el email ya existe (útil para validación)
    public boolean existsByEmail(String email) {
        return usuarioRepository.findByEmail(email).isPresent();
    }

    // Método adicional: Verificar si el DNI ya existe (útil para validación)
    public boolean existsByDni(String dni) {
        return usuarioRepository.existsByDni(dni);
    }
}