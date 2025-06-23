package com.example.Meteorite;

import com.example.Meteorite.model.Producto;
import com.example.Meteorite.repository.ProductoRepository;
import com.example.Meteorite.service.ProductoService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    @Test
    void testGetProductoById_found() {
        Producto p = new Producto(500, "Manjarate");
        p.setId(1L);
        when(productoRepository.findById(1L)).thenReturn(Optional.of(p));

        Optional<Producto> result = productoService.getProductoById(1L);

        assertTrue(result.isPresent());
        assertEquals("Manjarate", result.get().getName());
        verify(productoRepository).findById(1L);
    }

    @Test
    void testGetProductoById_notFound() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Producto> result = productoService.getProductoById(99L);

        assertFalse(result.isPresent());
        verify(productoRepository).findById(99L);
    }

    @Test
    void testCreateProducto() {
        Producto input = new Producto(2500, "Doritos");
        Producto saved = new Producto(3000, "Papas Lays");
        saved.setId(2L);

        // Cualquiera que entre a save devuelve 'saved'
        when(productoRepository.save(any(Producto.class))).thenReturn(saved);

        Producto result = productoService.createProducto(input);

        assertNotNull(result.getId(), "Debe asignar un ID al producto creado");
        assertEquals("Papas Lays", result.getName(), "El nombre debe venir del objeto guardado");
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    void testUpdateProducto_found() {
        Producto existing = new Producto(300, "Chocolate");
        existing.setId(3L);
        Producto updates = new Producto(1000, "Chocolate Premium");

        when(productoRepository.findById(3L)).thenReturn(Optional.of(existing));
        when(productoRepository.save(any(Producto.class))).thenAnswer(inv -> {
            Producto p = inv.getArgument(0);
            p.setId(3L);
            return p;
        });

        Producto result = productoService.updateProducto(3L, updates);

        assertEquals(3L, result.getId());
        assertEquals("Chocolate Premium", result.getName());
        verify(productoRepository).findById(3L);
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    void testUpdateProducto_notFound() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
            () -> productoService.updateProducto(99L, new Producto()));
        verify(productoRepository).findById(99L);
    }

    @Test
    void testDeleteProducto() {
        // deleteById no lanza excepción si existe
        doNothing().when(productoRepository).deleteById(4L);

        productoService.deleteProducto(4L);

        verify(productoRepository).deleteById(4L);
    }
}
