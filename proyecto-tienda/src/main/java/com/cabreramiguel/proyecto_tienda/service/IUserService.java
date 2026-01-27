package com.cabreramiguel.proyecto_tienda.service;

import java.util.List;

import com.cabreramiguel.proyecto_tienda.model.User;;

/*
 * Contrato para ProductServiceImpl
*/ 
public interface IUserService {
    
    List<User> findAll();

    User save(User user);

    boolean existsByUsername(String username);
}
