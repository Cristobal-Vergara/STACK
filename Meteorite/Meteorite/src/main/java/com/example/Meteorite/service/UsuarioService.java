package com.example.Meteorite.service;

import com.example.Meteorite.model.Usuario;
import com.example.Meteorite.repository.UsuarioRepository;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository UsuarioRepository;

    @PostConstruct
    public void init() {
        if (UsuarioRepository.count() == 0) {
            UsuarioRepository.save(new Usuario("admin", "admin"));
            UsuarioRepository.save(new Usuario("Cuchufli123", "1234"));
            UsuarioRepository.save(new Usuario("chimaux", "palta123"));
        }
    }

    public List<Usuario> getAllUsuarios() {
        return UsuarioRepository.findAll();
    }

    public Optional<Usuario> getUsuarioById(Long id) {
        return UsuarioRepository.findById(id);
    }

    public Usuario createUsuario(Usuario Usuario) {
        return UsuarioRepository.save(Usuario);
    }

    public Usuario updateUsuario(Long id, Usuario UsuarioDetails) {
        return UsuarioRepository.findById(id).map(Usuario -> {
            Usuario.setUsername(UsuarioDetails.getUsername());
            Usuario.setPassword(UsuarioDetails.getPassword());
            return UsuarioRepository.save(Usuario);
        }).orElseThrow(() -> new EntityNotFoundException("Usuario con ID " + id + " no encontrado"));
    }

    public void deleteUsuario(Long id) {
        UsuarioRepository.deleteById(id);
    }
}
