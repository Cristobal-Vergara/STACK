package com.example.Meteorite.service;

import com.example.Meteorite.model.Proveedor;
import com.example.Meteorite.repository.ProveedorRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProveedorService {

    @Autowired
    private ProveedorRepository ProveedorRepository;


    public List<Proveedor> getAllProveedor() {
        return ProveedorRepository.findAll();
    }

    public Optional<Proveedor> getProveedorById(Long id) {
        return ProveedorRepository.findById(id);
    }

    public Proveedor createProveedor(Proveedor proveedor) {
        return ProveedorRepository.save(proveedor);
    }

    public Proveedor updateProveedor(Long id, Proveedor ProveedorDetails) {
        return ProveedorRepository.findById(id).map(Proveedor -> {
            Proveedor.setNombreEmpresa(ProveedorDetails.getNombreEmpresa());
            Proveedor.setProducto(ProveedorDetails.getProducto());
            return ProveedorRepository.save(Proveedor);
        }).orElseThrow(() -> new EntityNotFoundException("Proveedor con ID " + id + " no encontrado"));
    }
    // Método para eliminar un proveedor por su ID
    public void deleteProveedor(Long id) {
        ProveedorRepository.deleteById(id);
    }
}
