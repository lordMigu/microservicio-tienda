package com.cabreramiguel.proyecto_tienda.controller;

import com.cabreramiguel.proyecto_tienda.model.Producto;
import com.cabreramiguel.proyecto_tienda.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {
    @Autowired
    private ProductoService productoServ;

    @PostMapping("/crear")
    @ResponseStatus(HttpStatus.CREATED)
    public String crearProducto(@RequestBody Producto producto){
        Producto productoGuardado = productoServ.createProducto(producto);
        return "Producto creado, Id: "+ productoGuardado.getCodigo_producto();
    }

    @GetMapping()
    public List<Producto> getProductos(){
        return productoServ.getProductos();
    }

    @GetMapping("/{Id}")
    public Producto getProductoId(@PathVariable Long Id){
        return productoServ.getProductoId(Id);
    }

    @PutMapping("/editar/{Id}")
    public Producto editProductoId(@PathVariable Long Id, @RequestBody Producto producto){
        return productoServ.updateProductoId(Id, producto);
    }

    @DeleteMapping("/eliminar/{Id}")
    public String deleteProductoId(@PathVariable Long Id){
        productoServ.deleteProductoId(Id);
        return "Producto eliminado";
    }

    //Falta stock, cantidad disponible menor a 5
    @GetMapping("/falta_stock")
    public List<Producto> getProductosMenorAcinco(){
        List<Producto> todosLosProductos = productoServ.getProductos();
        List<Producto> pocosProductos = new ArrayList<Producto>();
        for (Producto p : todosLosProductos){
            if (p.getCantidad_disponible() < 5){
                pocosProductos.add(p);
            }
        }
        return pocosProductos;
    }
}
