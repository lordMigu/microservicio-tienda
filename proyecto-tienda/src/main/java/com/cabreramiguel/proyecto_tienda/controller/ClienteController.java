package com.cabreramiguel.proyecto_tienda.controller;

import com.cabreramiguel.proyecto_tienda.model.Cliente;
import com.cabreramiguel.proyecto_tienda.service.ClienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/clientes")
@Tag(name = "Clientes", description = "Gestión de clientes")
public class ClienteController {
    @Autowired
    private ClienteService clienteServ;

    @PostMapping("/crear")
    @Operation(summary = "Crear Cliente", description = "Crea un cliente con nombre, apellido y dni")
    public ResponseEntity<Cliente> crearCliente(@RequestBody Cliente cliente){
        Cliente clienteGuardado = clienteServ.createCliente(cliente);
        return ResponseEntity.created(URI.create("/clientes/crear" + clienteGuardado.getId_cliente())).body(clienteGuardado);
    }

    @GetMapping()
    @Operation(summary = "Ver Clientes", description = "Muestra los clientes, o lanza un 404")
    public ResponseEntity<?> getClientes(){
        Optional<List<Cliente>> optionClientes = Optional.ofNullable(clienteServ.getClientes());
        if (optionClientes.isPresent()) {
            return ResponseEntity.ok(optionClientes.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{Id}")
    @Operation(summary = "Ver un cliente", description = "Ver cliente segun su Id")
    public ResponseEntity<?> getClientes(@PathVariable Long Id){
        Optional<Cliente> optionCliente = Optional.ofNullable(clienteServ.getClienteId(Id));
        if (optionCliente.isPresent()) {
            return ResponseEntity.ok(optionCliente.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/editar/{Id}")
    @Operation(summary = "Editar cliente", description = "Ingrese el id del cliente y el cuerpo completo del json a editar, si no existe el id, lanza 404")
    public ResponseEntity<?> editClienteId(@PathVariable Long Id, @RequestBody Cliente cliente){
        Optional<Cliente> optionCliente = clienteServ.updateClienteId(Id, cliente);
        if (optionCliente.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(optionCliente.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar/{Id}")
    @Operation(summary = "Borrar Cliente", description = "Borrar un cliente con su Id, si encuentra devuelve el cliente, y si no encuentra devuelve 404")
    public ResponseEntity<?> deleteClienteId(@PathVariable Long Id){
        Optional<Cliente> optionCliente = Optional.ofNullable(clienteServ.getClienteId(Id));
        if (optionCliente.isPresent()) {
            clienteServ.deleteClienteId(Id);
            return ResponseEntity.ok(optionCliente.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }
}
