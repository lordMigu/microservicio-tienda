package com.cabreramiguel.proyecto_tienda.service;

import com.cabreramiguel.proyecto_tienda.model.Cliente;
import com.cabreramiguel.proyecto_tienda.model.Producto;
import com.cabreramiguel.proyecto_tienda.repository.IProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService implements IProductoService{
    @Autowired
    private IProductoRepository productRepo;

    @Override
    public Producto createProducto(Producto producto) {
        return productRepo.save(producto);
    }

    @Override
    public List<Producto> getProductos() {
        return productRepo.findAll();
    }

    @Override
    public Producto getProductoId(Long Id) {
        return productRepo.findById(Id).orElse(null);
    }

    @Override
    public Producto updateProductoId(Long Id, Producto producto) {
        Producto productoExistente = productRepo.findById(Id).orElse(null);

        if (productoExistente != null) {
            // Actualizar campos manualmente
            productoExistente.setNombre(producto.getNombre());
            productoExistente.setMarca(producto.getMarca());
            productoExistente.setCosto(producto.getCosto());
            productoExistente.setCantidad_disponible(producto.getCantidad_disponible());

            // Guardar el producto actualizado
            productRepo.save(productoExistente);
            return productoExistente;
        } else {
            // Manejo de error si no se encuentra el producto
            throw new RuntimeException("Cliente con ID " + Id + " no encontrado.");
        }
    }

    @Override
    public void deleteProductoId(Long Id) {
        productRepo.deleteById(Id);
    }
}
