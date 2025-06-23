package com.example.Meteorite.repository;
import org.springframework.stereotype.Repository;
import com.example.Meteorite.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> { 

}
