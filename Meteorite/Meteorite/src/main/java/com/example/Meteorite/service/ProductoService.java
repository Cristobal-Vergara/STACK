package com.example.Meteorite.service;

import com.example.Meteorite.model.Producto;
import com.example.Meteorite.repository.ProductoRepository;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @PostConstruct
    public void init() {
        if (productoRepository.count() == 0) {
             productoRepository.save(new Producto(300, "Serranita"));
             productoRepository.save(new Producto(500.0, "Doritos"));
             productoRepository.save(new Producto(100.0, "Frugele"));
             productoRepository.save(new Producto(200.0, "Cheetos"));
             productoRepository.save(new Producto(400.0, "Papas"));
        }
     }
    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> getProductoById(Long id) {
        return productoRepository.findById(id);
    }

    public Producto createProducto(Producto producto) {
        return productoRepository.save(producto);
    }

   public Producto updateProducto(Long id, Producto productoDetails) {
    return productoRepository.findById(id)
        .map(producto -> {
            producto.setName(productoDetails.getName());
            producto.setPrice(productoDetails.getPrice());
            return productoRepository.save(producto);
        })
        .orElseThrow(() -> new EntityNotFoundException("Producto con ID " + id + " no encontrado"));
}

    public void deleteProducto(Long id) {
        productoRepository.deleteById(id);
    }
}
