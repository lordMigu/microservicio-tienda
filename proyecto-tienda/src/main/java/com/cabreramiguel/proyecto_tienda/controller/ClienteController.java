package com.cabreramiguel.proyecto_tienda.controller;

import com.cabreramiguel.proyecto_tienda.model.Cliente;
import com.cabreramiguel.proyecto_tienda.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
    @Autowired
    private ClienteService clienteServ;

    @PostMapping("/crear")
    @ResponseStatus(HttpStatus.CREATED)
    public String crearCliente(@RequestBody Cliente cliente){
        Cliente clienteGuardado = clienteServ.createCliente(cliente);
        return "Cliente creado, Id: "+ clienteGuardado.getId_cliente();
    }

    @GetMapping()
    public List<Cliente> getClientes(){
        return clienteServ.getClientes();
    }

    @GetMapping("/{Id}")
    public Cliente getClientes(@PathVariable Long Id){
        return clienteServ.getClienteId(Id);
    }

    @PutMapping("/editar/{Id}")
    public Cliente editClienteId(@PathVariable Long Id, @RequestBody Cliente cliente){
        return clienteServ.updateClienteId(Id, cliente);
    }

    @DeleteMapping("/eliminar/{Id}")
    public String deleteClienteId(@PathVariable Long Id){
        clienteServ.deleteClienteId(Id);
        return "Cliente eliminado";
    }
}
