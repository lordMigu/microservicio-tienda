package com.cabreramiguel.proyecto_tienda.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.cabreramiguel.proyecto_tienda.model.User;;

public interface UserRepository extends CrudRepository<User, Long>{
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);
}
