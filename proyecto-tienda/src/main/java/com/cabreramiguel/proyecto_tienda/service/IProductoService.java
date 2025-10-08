package com.cabreramiguel.proyecto_tienda.service;

import com.cabreramiguel.proyecto_tienda.model.Producto;

import java.util.List;

public interface IProductoService {
    Producto createProducto(Producto producto);
    List<Producto> getProductos();
    Producto getProductoId(Long Id);
    Producto updateProductoId(Long Id, Producto producto);
    void deleteProductoId(Long Id);    
}
