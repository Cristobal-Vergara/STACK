package com.example.Meteorite.repository;
import org.springframework.stereotype.Repository;
import com.example.Meteorite.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> { //Tiene que ser Integer y no int porque JPA solo acepta datos no primitivos
    // No es necesario agregar métodos adicionales, ya que JpaRepository proporciona métodos CRUD básicos.
    

}
