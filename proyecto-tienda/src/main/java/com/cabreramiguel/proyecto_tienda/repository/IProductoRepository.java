package com.cabreramiguel.proyecto_tienda.repository;

import com.cabreramiguel.proyecto_tienda.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductoRepository extends JpaRepository<Producto, Long> {
}
