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
        // Buscamos por email al usuario
        Optional<UsuarioSistema> usuarioOpt = usuarioRepository.findByEmail(email);

        // Si existe, verificamos la contraseña
        if (usuarioOpt.isPresent()) {
            UsuarioSistema usuario = usuarioOpt.get();
            if (usuario.getPassword().equals(password)) {
                return usuario; // Login exitoso
            }
        }
        return null; // Login fallido
    }
    // Método para registrar un nuevo usuario
    public UsuarioSistema guardarUsuario(UsuarioSistema usuario) {
        return usuarioRepository.save(usuario);
    }

    // Método Verificar si el email ya existe
    public boolean existsByEmail(String email) {
        return usuarioRepository.findByEmail(email).isPresent();
    }
    // Método Verificar si el DNI ya existe
    public boolean existsByDni(String dni) {
        return usuarioRepository.findByDni(dni).isPresent();
    }
}