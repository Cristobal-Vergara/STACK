package com.example.Meteorite.service;

import com.example.Meteorite.model.Persona;
import com.example.Meteorite.repository.PersonaRepository;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonaService {

    @Autowired
    private PersonaRepository personaRepository;

    @PostConstruct
    public void init() {
        if (personaRepository.count() == 0) {
            personaRepository.save(new Persona("15.345.222-7", "Manuel", "Pérez", "González"));
            personaRepository.save(new Persona("15.345.222-8", "Juan", "cofre", "flores"));
            personaRepository.save(new Persona("15.345.222-9", "Pedro", "sepulveda", "gutierrez"));
            personaRepository.save(new Persona("15.345.222-10", "Pablo", "jimenez", "santibañez"));
    
        }
    }

    public List<Persona> getAllPersonas() {
        return personaRepository.findAll();
    }

    public Optional<Persona> getPersonaByid(Long id) {
        return personaRepository.findById(id); 
    }

    public Persona createPersona(Persona persona) {
        return personaRepository.save(persona);
    }

    public Persona updatePersona(Long id, Persona PersonaDetails) {
        return personaRepository.findById(id).map(Persona -> {
            Persona.setNombre(PersonaDetails.getNombre());
            Persona.setApellidoP(PersonaDetails.getApellidoP());
            return personaRepository.save(Persona);
        }).orElseThrow(() -> new EntityNotFoundException("Persona con ID " + id + " no encontrado"));
        
    }

    public void deletePersona(Long id) {
        personaRepository.deleteById(id);
    }
}
