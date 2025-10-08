package com.cabreramiguel.proyecto_tienda.repository;

import com.cabreramiguel.proyecto_tienda.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IVentaRepository extends JpaRepository<Venta, Long> {
}
