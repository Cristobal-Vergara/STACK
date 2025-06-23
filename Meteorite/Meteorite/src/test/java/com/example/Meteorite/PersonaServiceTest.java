package com.example.Meteorite;

import com.example.Meteorite.model.Persona;
import com.example.Meteorite.repository.PersonaRepository;
import com.example.Meteorite.service.PersonaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class PersonaServiceTest {

    @Mock
    private PersonaRepository personaRepository;

    @InjectMocks
    private PersonaService personaService;

    @Test
    void testGetPersonaById_found() {
        Persona persona = new Persona("15.345.222-7", "Manuel", "Pérez", "González");
        persona.setId(1L);
        when(personaRepository.findById(1L)).thenReturn(Optional.of(persona));

        Optional<Persona> result = personaService.getPersonaByid(1L);

        assertTrue(result.isPresent());
        assertEquals("Manuel", result.get().getNombre());
        verify(personaRepository).findById(1L);
    }

    @Test
    void testGetPersonaById_notFound() {
        when(personaRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Persona> result = personaService.getPersonaByid(99L);

        assertFalse(result.isPresent());
        verify(personaRepository).findById(99L);
    }

    @Test
    void testCreatePersona() {
        Persona input = new Persona("22.333.444-5", "María", "López", "García");
        input.setId(null);
        Persona saved = new Persona("22.333.444-5", "María", "López", "García");
        saved.setId(2L);

        when(personaRepository.save(input)).thenReturn(saved);

        Persona result = personaService.createPersona(input);

        assertNotNull(result.getId());
        assertEquals("María", result.getNombre());
        verify(personaRepository).save(input);
    }

    @Test
    void testUpdatePersona_found() {
        Persona existing = new Persona("11.222.333-4", "Luis", "Fernández", "Ruiz");
        existing.setId(3L);
        Persona updates = new Persona("11.222.333-4", "Luis Mod", "Fernández", "Ruiz");
        updates.setId(3L);
      
        when(personaRepository.findById(3L)).thenReturn(Optional.of(existing));
        when(personaRepository.save(any(Persona.class)))
            .thenAnswer(inv -> {
                Persona p = inv.getArgument(0);
                return p;
            });

        Persona result = personaService.updatePersona(3L, updates);

        assertEquals(3L, result.getId());
        assertEquals("Luis Mod", result.getNombre());
        verify(personaRepository).findById(3L);
        verify(personaRepository).save(updates);
    }

    @Test
    void testUpdatePersona_notFound() {
        when(personaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
            () -> personaService.updatePersona(99L, new Persona()));
        verify(personaRepository).findById(99L);
    }

    @Test
    void testDeletePersona() {
        // No lanzamos nada si existe
        doNothing().when(personaRepository).deleteById(4L);

        personaService.deletePersona(4L);

        verify(personaRepository).deleteById(4L);
    }
}
