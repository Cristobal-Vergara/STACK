package com.example.Meteorite.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {
    @Id 
    @GeneratedValue
    private Long id;
    private double price;
    private String name;

    public Producto(double price, String name) {
        this.price = price;
        this.name = name;
    }
}
