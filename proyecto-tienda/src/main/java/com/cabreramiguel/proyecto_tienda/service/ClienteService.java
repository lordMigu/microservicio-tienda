package com.cabreramiguel.proyecto_tienda.service;

import com.cabreramiguel.proyecto_tienda.model.Cliente;
import com.cabreramiguel.proyecto_tienda.repository.IClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService implements IClienteService{
    @Autowired
    private IClienteRepository clienteRepo;

    @Override
    public Cliente createCliente(Cliente cliente){
        return clienteRepo.save(cliente);
    }

    @Override
    public List<Cliente> getClientes() {
        return clienteRepo.findAll();
    }

    @Override
    public Cliente getClienteId(Long Id) {
        return clienteRepo.findById(Id).orElse(null);
    }

    @Override
    public Cliente updateClienteId(Long id, Cliente cliente) {
        Cliente clienteExistente = clienteRepo.findById(id).orElse(null);

        if (clienteExistente != null) {
            // Actualizar campos manualmente
            clienteExistente.setNombre(cliente.getNombre());
            clienteExistente.setApellido(cliente.getApellido());
            clienteExistente.setDni(cliente.getDni());

            // Guardar el cliente actualizado
            clienteRepo.save(clienteExistente);
            return clienteExistente;
        } else {
            // Manejo de error si no se encuentra el cliente
            throw new RuntimeException("Cliente con ID " + id + " no encontrado.");
        }
    }


    @Override
    public void deleteClienteId(Long Id) {
        clienteRepo.deleteById(Id);
    }
}
