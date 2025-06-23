package com.example.Meteorite;

import com.example.Meteorite.model.Usuario;
import com.example.Meteorite.repository.UsuarioRepository;
import com.example.Meteorite.service.UsuarioService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void testGetAllUsuarios() {
        Usuario u1 = new Usuario("admin", "admin");
        Usuario u2 = new Usuario("user", "1234");

        when(usuarioRepository.findAll()).thenReturn(List.of(u1, u2));

        List<Usuario> usuarios = usuarioService.getAllUsuarios();

        assertEquals(2, usuarios.size());
        assertEquals("admin", usuarios.get(0).getUsername());
        verify(usuarioRepository).findAll();
    }

    @Test
    void testGetUsuarioById_found() {
        Usuario usuario = new Usuario("admin", "admin");
        usuario.setId(1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Optional<Usuario> result = usuarioService.getUsuarioById(1L);

        assertTrue(result.isPresent());
        assertEquals("admin", result.get().getUsername());
        verify(usuarioRepository).findById(1L);
    }

    @Test
    void testGetUsuarioById_notFound() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Usuario> result = usuarioService.getUsuarioById(99L);

        assertFalse(result.isPresent());
        verify(usuarioRepository).findById(99L);
    }

    @Test
    void testCreateUsuario() {
        Usuario nuevo = new Usuario("nuevoUser", "clave123");

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(nuevo);

        Usuario result = usuarioService.createUsuario(nuevo);

        assertEquals("nuevoUser", result.getUsername());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void testUpdateUsuario_found() {
        Usuario existente = new Usuario("viejo", "pass");
        existente.setId(1L);
        Usuario cambios = new Usuario("nuevo", "nuevaPass");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.save(any(Usuario.class)))
            .thenAnswer(inv -> {
                Usuario u = inv.getArgument(0);
                u.setId(1L);
                return u;
            });

        Usuario result = usuarioService.updateUsuario(1L, cambios);

        assertEquals(1L, result.getId());
        assertEquals("nuevo", result.getUsername());
        assertEquals("nuevaPass", result.getPassword());
        verify(usuarioRepository).findById(1L);
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void testUpdateUsuario_notFound() {
        Usuario cambios = new Usuario("x", "x");
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
            () -> usuarioService.updateUsuario(99L, cambios));

        verify(usuarioRepository).findById(99L);
    }

    @Test
    void testDeleteUsuario() {
        doNothing().when(usuarioRepository).deleteById(7L);

        usuarioService.deleteUsuario(7L);

        verify(usuarioRepository).deleteById(7L);
    }
}
