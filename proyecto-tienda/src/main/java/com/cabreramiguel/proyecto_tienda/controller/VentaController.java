package com.cabreramiguel.proyecto_tienda.controller;

import com.cabreramiguel.proyecto_tienda.dto.VentaMayorDTO;
import com.cabreramiguel.proyecto_tienda.model.Producto;
import com.cabreramiguel.proyecto_tienda.model.Venta;
import com.cabreramiguel.proyecto_tienda.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/ventas")
public class VentaController {
    @Autowired
    private VentaService ventaServ;

    @PostMapping("/crear")
    @ResponseStatus(HttpStatus.CREATED)
    public String crearVenta(@RequestBody Venta venta){
        Venta ventaGuardado = ventaServ.createVenta(venta);
        return "Venta creado, Id: "+ ventaGuardado.getCodigo_venta();
    }

    @GetMapping()
    public List<Venta> getVentas(){
        return ventaServ.getVentas();
    }

    @GetMapping("/{Id}")
    public Venta getVentaId(@PathVariable Long Id){
        return ventaServ.getVentaId(Id);
    }

    @PutMapping("/editar/{Id}")
    public Venta editVentaId(@PathVariable Long Id, @RequestBody Venta venta){
        return ventaServ.updateVentaId(Id, venta);
    }

    @DeleteMapping("/eliminar/{Id}")
    public String deleteVentaId(@PathVariable Long Id){
        ventaServ.deleteVentaId(Id);
        return "Venta eliminada";
    }

    @GetMapping("/productos/{codigo_venta}")
    public List<Producto> productosDeUnaVenta(@PathVariable Long codigo_venta){
        Venta ventaDelId = this.getVentaId(codigo_venta);
        return ventaDelId.getListaProductos();
    }

    @GetMapping("/montos/{fecha_venta}")
    public String MontoVentasDeUnDia(@PathVariable LocalDate fecha_venta){
        List<Venta> ventas = this.getVentas();
        int contadorVentas = 0;
        Double sumatoriaMonto = 0.0;
        for(Venta v :ventas){
            if(v.getFecha_venta().equals(fecha_venta)){
                sumatoriaMonto += v.getTotal();
                contadorVentas ++;
            }
        }
        return "La sumatoriaMonto: " + sumatoriaMonto + ", y el numero de ventas: "+contadorVentas;
    }

    @GetMapping("/mayor_venta")
    public VentaMayorDTO getVentaMayorMonto(){
        List<Venta> todasLasVentas = this.getVentas();

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

        return ventaMayor;
    }
}
