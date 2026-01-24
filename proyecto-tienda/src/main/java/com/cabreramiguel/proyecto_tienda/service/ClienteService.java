package com.cabreramiguel.proyecto_tienda.service;

import com.cabreramiguel.proyecto_tienda.model.Cliente;
import com.cabreramiguel.proyecto_tienda.repository.IClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService implements IClienteService{
    @Autowired
    private IClienteRepository clienteRepo;

    @Override
    public Cliente createCliente(Cliente cliente){
        return clienteRepo.save(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> getClientes() {
        return clienteRepo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente getClienteId(Long Id) {
        return clienteRepo.findById(Id).orElse(null);
    }

    @Override
    public Optional<Cliente> updateClienteId(Long id, Cliente cliente) {
        Optional<Cliente> clienteExistente = clienteRepo.findById(id);

        if (clienteExistente.isPresent()) {
            // Actualizar campos manualmente
            Cliente clienteNoNulo = clienteExistente.get();
            clienteNoNulo.setNombre(cliente.getNombre());
            clienteNoNulo.setApellido(cliente.getApellido());
            clienteNoNulo.setDni(cliente.getDni());

            // Guardar el cliente actualizado
            clienteRepo.save(clienteNoNulo);
            return Optional.of(clienteNoNulo);
        } 
        
        return clienteExistente;
    }


    @Override
    public void deleteClienteId(Long Id) {
        clienteRepo.deleteById(Id);
    }
}
