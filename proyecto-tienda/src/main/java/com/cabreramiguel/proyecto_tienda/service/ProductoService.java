package com.cabreramiguel.proyecto_tienda.service;

import com.cabreramiguel.proyecto_tienda.model.Producto;
import com.cabreramiguel.proyecto_tienda.repository.IProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(readOnly = true)
    public List<Producto> getProductos() {
        return productRepo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
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
        }
        return null;
    }

    @Override
    public void deleteProductoId(Long Id) {
        productRepo.deleteById(Id);
    }
}
