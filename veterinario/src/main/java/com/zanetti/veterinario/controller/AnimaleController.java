package com.zanetti.veterinario.controller;

import com.zanetti.veterinario.model.Animale;
import com.zanetti.veterinario.repository.AnimaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/animali")
public class AnimaleController {

    @Autowired
    private AnimaleRepository repo;

    //  Restituisce la lista completa degli animali in formato JSON
    @GetMapping
    public List<Animale> listaAnimali() {
        return repo.findAll();
    }

    // Aggiunge un nuovo animale in JSON
    @PostMapping
    public Animale aggiungiAnimale(@RequestBody Animale animale) {
        return repo.save(animale);
    }
}
