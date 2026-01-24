package com.cabreramiguel.proyecto_tienda.controller;

import com.cabreramiguel.proyecto_tienda.model.Producto;
import com.cabreramiguel.proyecto_tienda.model.Venta;
import com.cabreramiguel.proyecto_tienda.service.ProductoService;
import com.cabreramiguel.proyecto_tienda.service.VentaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/productos")
@Tag(name = "Productos", description = "Gestión de los productos")
public class ProductoController {
    @Autowired
    private ProductoService productoServ;

    @Autowired
    private VentaService ventaServ;   

    @PostMapping("/crear")
    @Operation(summary = "Crear un producto", description = "Ingresa un producto")
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto){
        Producto productoGuardado = productoServ.createProducto(producto);
        return ResponseEntity.created(URI.create("/productos/crear" + productoGuardado.getCodigo_producto())).body(productoGuardado);
    }

    @GetMapping()
    @Operation(summary = "Ver todos los producto", description = "Si no hay productos lanza un 404")
    public ResponseEntity<?> getProductos(){
        List<Producto> todosProductos = productoServ.getProductos();
        if (todosProductos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(todosProductos);
    }

    @GetMapping("/{Id}")
    @Operation(summary = "Obtener producto por ID", description = "Id del producto es necesario, si no encuentra lanza 404")
    public ResponseEntity<?> getProductoId(@PathVariable Long Id){        
        Optional<Producto> optionProducto = Optional.ofNullable(productoServ.getProductoId(Id));
        if (optionProducto.isPresent()) { return ResponseEntity.ok(optionProducto.orElseThrow());}
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/editar/{Id}")
    @Operation(summary = "Edita producto por ID", description = "Id del producto es necesario, si no encuentra lanza 404")
    public ResponseEntity<Producto> editProductoId(@PathVariable Long Id, @RequestBody Producto producto){
        Optional<Producto> optionProducto = Optional.ofNullable(productoServ.updateProductoId(Id, producto));
        if (optionProducto.isPresent()) { 
            return ResponseEntity.status(HttpStatus.CREATED).body(optionProducto.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar/{Id}")
    @Operation(summary = "Elimina producto por ID", description = "Id del producto es necesario, si encuentra devuelve el producto y si no encuentra lanza 404")
    public ResponseEntity<?> deleteProductoId(@PathVariable Long Id){
        Optional<Producto> optionProducto = Optional.ofNullable(productoServ.getProductoId(Id));
        if (optionProducto.isPresent()){
            productoServ.deleteProductoId(Id);
            return ResponseEntity.ok(optionProducto.orElseThrow());
        }        
        return ResponseEntity.notFound().build();
    }

    //Falta stock, cantidad disponible menor a 5
    @GetMapping("/falta_stock")
    @Operation(summary = "Obtener producto con stock menor a 5", description = "Si no hay productos lanza un 404. Si no hay menos de 5, lanza 404")
    public ResponseEntity<?> getProductosMenorAcinco(){
        Optional<List<Producto>> optionTodosLosProductos = Optional.ofNullable(productoServ.getProductos());
        List<Producto> pocosProductos = new ArrayList<Producto>();
        if (optionTodosLosProductos.isPresent()) {
            optionTodosLosProductos.get().stream()
                .filter(p -> p.getCantidad_disponible() < 5)
                .forEach(pocosProductos::add);
            if (pocosProductos.isEmpty()){ return ResponseEntity.notFound().build(); }
            return ResponseEntity.ok(pocosProductos);
        }
        return ResponseEntity.notFound().build();        
    }

    @GetMapping("/venta/{codigo_venta}")
    @Operation(summary = "Productos de una Venta", description = "Todos los productos relacionados a una Venta, segun su codigo_venta")
    public ResponseEntity<?> productosDeUnaVenta(@PathVariable Long codigo_venta){
        Optional<Venta> ventaDelId = Optional.ofNullable(ventaServ.getVentaId(codigo_venta));
        if (ventaDelId.isPresent()) {
            return ResponseEntity.ok(ventaDelId.orElseThrow().getListaProductos());            
        }
        return ResponseEntity.notFound().build();
    }
}
