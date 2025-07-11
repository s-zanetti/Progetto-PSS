package com.zanetti.veterinario.controller;

import com.zanetti.veterinario.model.Cliente;
import com.zanetti.veterinario.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/clienti")
public class ClienteController {

    @Autowired
    private ClienteRepository repo;

    // RItorna la lista di tutti i clienti in formato JSON
    @GetMapping
    public List<Cliente> listaClienti() {
        return repo.findAll();
    }

    // Registra un nuovo cliente via JSON
    @PostMapping
    public Cliente aggiungiCliente(@RequestBody @Valid Cliente cliente) {
        return repo.save(cliente);
    }
    
    
    
    
}
