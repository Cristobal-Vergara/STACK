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
public class Persona {
    @Id @GeneratedValue
    private Long id;
    private String rut; //CAMBIAR PORQUE ESTA GENERANDO AUTOMATICAMENTE EL RUT COMO ID, POR ESO EL POST NO FUNCIONABA
    private String nombre;
    private String apellidoP;
    private String apellidoM;

    public Persona(String rut, String nombre, String apellidoP, String apellidoM) {
        this.rut = rut; 
        this.nombre = nombre;
        this.apellidoP = apellidoP; 
        this.apellidoM = apellidoM;
    }
}