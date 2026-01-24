package com.cabreramiguel.proyecto_tienda.controller;

import com.cabreramiguel.proyecto_tienda.dto.VentaMayorDTO;
import com.cabreramiguel.proyecto_tienda.model.Venta;
import com.cabreramiguel.proyecto_tienda.service.VentaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ventas")
@Tag(name = "Ventas", description = "Gestión de ventas")
public class VentaController {
    @Autowired
    private VentaService ventaServ;

    @PostMapping("/crear")
    @Operation(summary = "Crear una venta", description = "Endpoint para crear uan venta con fecha_venta y total")    
    public ResponseEntity<Venta> crearVenta(@RequestBody Venta venta){        
        Venta ventaCreada = ventaServ.createVenta(venta);
        return ResponseEntity.created(URI.create("/ventas/crear" + ventaCreada.getCodigo_venta())).body(ventaCreada);
    }

    @GetMapping()
    @Operation(summary = "Listar todas las ventas", description = "Si no hay ventas, lanza un 404")
    public ResponseEntity<?> getVentas(){
        Optional<List<Venta>> optionVenta = Optional.ofNullable(ventaServ.getVentas());
        if (optionVenta.isPresent()) {
            return ResponseEntity.ok(optionVenta.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{Id}")
    @Operation(summary = "Ver una venta", description = "Ingresar el id de la venta, si no hay lanza un 404")
    public ResponseEntity<?> getVentaId(@PathVariable Long Id){
        Optional<Venta> optionVenta = Optional.ofNullable(ventaServ.getVentaId(Id));
        if (optionVenta.isPresent()) {
            return ResponseEntity.ok(optionVenta.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/editar/{Id}")
    @Operation(summary = "Editar una Venta", description = "Para editar, ingrese el id (si no hay, lanza 404), editar el json con fecha_venta y total")
    public ResponseEntity<?> editVentaId(@PathVariable Long Id, @RequestBody Venta venta){
        Optional<Venta> optionVenta = Optional.ofNullable(ventaServ.updateVentaId(Id, venta));
        if (optionVenta.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(optionVenta.orElseThrow());
        }  
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar/{Id}")
    @Operation(summary = "Eliminar una Venta", description = "Para eliminar, ingrese el id  (si no hay qué eliminar, lanza 404)")
    public ResponseEntity<?> deleteVentaId(@PathVariable Long Id){
        Optional<Venta> optionVenta = Optional.ofNullable(ventaServ.getVentaId(Id));
        if (optionVenta.isPresent()) {
            ventaServ.deleteVentaId(Id);
            return ResponseEntity.ok(optionVenta.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }    

    @GetMapping("/montos/{fecha_venta}")
    @Operation(summary = "Informacion de una venta", description = "Numero de ventas concretadas en una fecha y el monto sumado de todas las ventas")
    public ResponseEntity<?> MontoVentasDeUnDia(@PathVariable LocalDate fecha_venta){
        List<Venta> ventas = ventaServ.getVentas();
        Double contadorVentas = 0.0;
        Double sumatoriaMonto = 0.0;
        HashMap<String, Double> respuesta = new HashMap<>();
        for(Venta v :ventas){
            if(v.getFecha_venta().equals(fecha_venta)){
                sumatoriaMonto += v.getTotal();
                contadorVentas ++;
            }
        }
        respuesta.put("Sumatoria del monto", sumatoriaMonto);
        respuesta.put("Numero de ventas", contadorVentas);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/mayor_venta")
    @Operation(summary = "La mejor Venta", description = "El monto más alto de una venta")
    public ResponseEntity<?> getVentaMayorMonto(){
        List<Venta> todasLasVentas = ventaServ.getVentas();
        VentaMayorDTO ventaMayor = new VentaMayorDTO();
        Double mayorValor = 0.0;
        for (Venta v : todasLasVentas) {
            if (v.getTotal() > mayorValor) {
                mayorValor = v.getTotal();
                ventaMayor.setCodigo_venta(v.getCodigo_venta());
                ventaMayor.setTotal(v.getTotal());
                ventaMayor.setCantidad_productos(v.getListaProductos().size());
                ventaMayor.setNombre_cliente(v.getUnCliente().getNombre());
                ventaMayor.setApellido_cliente(v.getUnCliente().getApellido());
            }
        }
        return ResponseEntity.ok(ventaMayor);
    }
}
