package com.example.Meteorite;

import com.example.Meteorite.model.Producto;
import com.example.Meteorite.model.Proveedor;
import com.example.Meteorite.repository.ProveedorRepository;
import com.example.Meteorite.service.ProveedorService;

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
class ProveedorServiceTest {

    @Mock
    private ProveedorRepository proveedorRepository;

    @InjectMocks
    private ProveedorService proveedorService;

    @Test
    void testGetAllProveedor() {
        Producto producto = new Producto(1000, "Cereal");
        Proveedor p1 = new Proveedor("12345678-9", "Empresa A", producto, 123456789);
        Proveedor p2 = new Proveedor("98765432-1", "Empresa B", producto, 987654321);

        when(proveedorRepository.findAll()).thenReturn(List.of(p1, p2));

        List<Proveedor> proveedores = proveedorService.getAllProveedor();

        assertEquals(2, proveedores.size());
        assertEquals("Empresa A", proveedores.get(0).getNombreEmpresa());
        verify(proveedorRepository).findAll();
    }

    @Test
    void testGetProveedorById_found() {
        Producto producto = new Producto(500, "Leche");
        Proveedor proveedor = new Proveedor("12345678-9", "Empresa Uno", producto, 987654321);
        proveedor.setId(1L);

        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(proveedor));

        Optional<Proveedor> result = proveedorService.getProveedorById(1L);

        assertTrue(result.isPresent());
        assertEquals("Empresa Uno", result.get().getNombreEmpresa());
        verify(proveedorRepository).findById(1L);
    }

    @Test
    void testGetProveedorById_notFound() {
        when(proveedorRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Proveedor> result = proveedorService.getProveedorById(99L);

        assertFalse(result.isPresent());
        verify(proveedorRepository).findById(99L);
    }

    @Test
    void testCreateProveedor() {
        Producto producto = new Producto(2000, "Galletas");
        Proveedor nuevo = new Proveedor("11222333-4", "Proveedor X", producto, 555666777);

        when(proveedorRepository.save(any(Proveedor.class))).thenReturn(nuevo);

        Proveedor result = proveedorService.createProveedor(nuevo);

        assertEquals("Proveedor X", result.getNombreEmpresa());
        verify(proveedorRepository).save(any(Proveedor.class));
    }

    @Test
    void testUpdateProveedor_found() {
        Producto original = new Producto(800, "Jugo");
        Producto actualizado = new Producto(900, "Jugo Premium");

        Proveedor existente = new Proveedor("99988877-6", "Antiguo", original, 111111111);
        existente.setId(2L);
        Proveedor cambios = new Proveedor("99988877-6", "Nuevo", actualizado, 222222222);

        when(proveedorRepository.findById(2L)).thenReturn(Optional.of(existente));
        when(proveedorRepository.save(any(Proveedor.class)))
            .thenAnswer(inv -> {
                Proveedor p = inv.getArgument(0);
                p.setId(2L);
                return p;
            });

        Proveedor result = proveedorService.updateProveedor(2L, cambios);

        assertEquals(2L, result.getId());
        assertEquals("Nuevo", result.getNombreEmpresa());
        assertEquals(actualizado, result.getProducto());
        verify(proveedorRepository).findById(2L);
        verify(proveedorRepository).save(any(Proveedor.class));
    }

    @Test
    void testUpdateProveedor_notFound() {
        Proveedor cambios = new Proveedor("00000000-0", "No existe", new Producto(), 111111111);

        when(proveedorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
            () -> proveedorService.updateProveedor(99L, cambios));
        verify(proveedorRepository).findById(99L);
    }

    @Test
    void testDeleteProveedor() {
        doNothing().when(proveedorRepository).deleteById(5L);

        proveedorService.deleteProveedor(5L);

        verify(proveedorRepository).deleteById(5L);
    }
}
