package com.cabreramiguel.proyecto_tienda.service;

import com.cabreramiguel.proyecto_tienda.model.Venta;

import java.util.List;

public interface IVentaService {
    Venta createVenta(Venta venta);
    List<Venta> getVentas();
    Venta getVentaId(Long Id);
    Venta updateVentaId(Long Id, Venta venta);
    void deleteVentaId(Long Id);
}
