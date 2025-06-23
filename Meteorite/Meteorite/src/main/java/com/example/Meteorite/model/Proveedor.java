package com.example.Meteorite.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Proveedor {
    @Id @GeneratedValue
    private Long id;
    private String rutEmpresa;
    private String nombreEmpresa;
    @ManyToOne // Relación con Producto
    // Se asume que un proveedor puede tener un producto
    private Producto producto;
    private int fono;

    public Proveedor(String rutEmpresa, String nombreEmpresa, Producto producto, int fono) {
        this.rutEmpresa = rutEmpresa;
        this.nombreEmpresa = nombreEmpresa;
        this.producto = producto;
        this.fono = fono;
    }
}
