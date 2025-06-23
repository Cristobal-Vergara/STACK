package com.example.Meteorite.repository;
import org.springframework.stereotype.Repository;
import com.example.Meteorite.model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> { 

}
