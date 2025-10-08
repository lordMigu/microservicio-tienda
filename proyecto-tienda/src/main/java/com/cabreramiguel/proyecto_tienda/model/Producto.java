package com.cabreramiguel.proyecto_tienda.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long codigo_producto;
    private String nombre;
    private String marca;
    private Double costo;
    private Double cantidad_disponible;

    @ManyToOne
    @JoinColumn(name = "fk_cod_venta", referencedColumnName = "codigo_venta") //nombre de la columna del fk
    @JsonBackReference
    private Venta p_venta;



    public Producto() {
    }

    public Producto(Long codigo_producto, String nombre, String marca, Double costo, Double cantidad_disponible, Venta p_venta) {
        this.codigo_producto = codigo_producto;
        this.nombre = nombre;
        this.marca = marca;
        this.costo = costo;
        this.cantidad_disponible = cantidad_disponible;
        this.p_venta = p_venta;
    }
}
