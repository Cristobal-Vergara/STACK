package com.example.Meteorite.repository;
import org.springframework.stereotype.Repository;
import com.example.Meteorite.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> { 

}
