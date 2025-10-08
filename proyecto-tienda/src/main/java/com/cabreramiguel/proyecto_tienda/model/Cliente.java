package com.cabreramiguel.proyecto_tienda.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id_cliente;
    private String nombre;
    private String apellido;
    private String dni;

    @OneToOne(mappedBy = "unCliente")
    @JsonBackReference
    private Venta c_venta;

    public Cliente() {
    }

    public Cliente(Long id_cliente, String nombre, String apellido, String dni, Venta c_venta) {
        this.id_cliente = id_cliente;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.c_venta = c_venta;
    }
}
