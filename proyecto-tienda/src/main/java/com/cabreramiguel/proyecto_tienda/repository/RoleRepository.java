package com.cabreramiguel.proyecto_tienda.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.cabreramiguel.proyecto_tienda.model.Role;;

public interface RoleRepository extends CrudRepository<Role, Long> {

    Optional<Role> findByName(String name);
    
}
