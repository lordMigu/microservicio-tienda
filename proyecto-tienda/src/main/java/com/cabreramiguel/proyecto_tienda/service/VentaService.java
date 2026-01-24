package com.cabreramiguel.proyecto_tienda.service;
import com.cabreramiguel.proyecto_tienda.model.Cliente;
import com.cabreramiguel.proyecto_tienda.model.Producto;
import com.cabreramiguel.proyecto_tienda.model.Venta;
import com.cabreramiguel.proyecto_tienda.repository.IClienteRepository;
import com.cabreramiguel.proyecto_tienda.repository.IProductoRepository;
import com.cabreramiguel.proyecto_tienda.repository.IVentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VentaService implements IVentaService{

    @Autowired
    private IVentaRepository ventaRepo;
    @Autowired
    private IClienteRepository clienteRepo;
    @Autowired
    private IProductoRepository productoRepo;
    
    @Override
    public Venta createVenta(Venta venta) {
        // Buscar cliente
        Cliente cliente = clienteRepo.findById(venta.getUnCliente().getId_cliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        venta.setUnCliente(cliente);

        // Guardar venta primero
        Venta ventaGuardada = ventaRepo.save(venta);

        // Asignar venta a cliente (bidireccional)
        cliente.setC_venta(ventaGuardada);
        clienteRepo.save(cliente);

        // Asignar venta a productos
        List<Producto> productos = new ArrayList<>();
        for (Producto p : venta.getListaProductos()) {
            Producto producto = productoRepo.findById(p.getCodigo_producto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            producto.setP_venta(ventaGuardada);
            productos.add(producto);
        }

        productoRepo.saveAll(productos);
        ventaGuardada.setListaProductos(productos);

        return ventaGuardada;
    }

    @Override
    public List<Venta> getVentas() {
        return ventaRepo.findAll();
    }

    @Override
    public Venta getVentaId(Long Id) {
        return ventaRepo.findById(Id).orElse(null);
    }

    @Override
    public Venta updateVentaId(Long Id, Venta venta) {
        Venta ventaExistente = ventaRepo.findById(Id).orElse(null);

        if (ventaExistente != null) {
            // Actualizar campos simples
            ventaExistente.setFecha_venta(venta.getFecha_venta());
            ventaExistente.setTotal(venta.getTotal());

            // Asignar cliente existente
            Cliente cliente = clienteRepo.findById(venta.getUnCliente().getId_cliente())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            ventaExistente.setUnCliente(cliente);
            cliente.setC_venta(ventaExistente);
            clienteRepo.save(cliente);

            // Asignar productos existentes
            List<Producto> productosActualizados = new ArrayList<>();
            for (Producto p : venta.getListaProductos()) {
                Producto producto = productoRepo.findById(p.getCodigo_producto())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
                producto.setP_venta(ventaExistente);
                productosActualizados.add(producto);
            }
            ventaExistente.setListaProductos(productosActualizados);
            productoRepo.saveAll(productosActualizados);

            // Guardar venta actualizada
            return ventaRepo.save(ventaExistente);
        } else {
            throw new RuntimeException("Venta con ID " + Id + " no encontrado.");
        }
    }

    @Override
    public void deleteVentaId(Long Id) {
        ventaRepo.deleteById(Id);
    }
}
