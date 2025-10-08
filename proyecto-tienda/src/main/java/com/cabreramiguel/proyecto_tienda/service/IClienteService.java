package com.cabreramiguel.proyecto_tienda.service;

import com.cabreramiguel.proyecto_tienda.model.Cliente;

import java.util.List;

public interface IClienteService {
    Cliente createCliente(Cliente cliente);
    List<Cliente> getClientes();
    Cliente getClienteId(Long Id);
    Cliente updateClienteId(Long Id, Cliente cliente);
    void deleteClienteId(Long Id);
}

