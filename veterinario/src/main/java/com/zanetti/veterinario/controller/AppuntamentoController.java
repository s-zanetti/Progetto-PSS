package com.zanetti.veterinario.controller;

import com.zanetti.veterinario.model.Appuntamento;
import com.zanetti.veterinario.repository.AppuntamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appuntamenti")
public class AppuntamentoController {

    @Autowired
    private AppuntamentoRepository repo;

    // Ritorna tutti gli appuntamenti in formato JSON
    @GetMapping
    public List<Appuntamento> listaAppuntamenti() {
        return repo.findAll();
    }

    
    @PostMapping
    public ResponseEntity<?> aggiungiAppuntamento(@RequestBody Appuntamento nuovo) {
        // Verifica se la data è nel passato rispetto a ora
        if (nuovo.getDataOra().isBefore(java.time.LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data non disponibile, riprovare");
        }
        
        boolean esiste = repo.existsByDataOra(nuovo.getDataOra());
        
        // Verifica fascia oraria
        if (esiste) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Questa fascia oraria è già stata prenotata");
        }
        
        repo.save(nuovo);
        return ResponseEntity.ok(nuovo);
    }

    // Lista orari occupati
    @GetMapping("/orari-occupati")
    public List<String> orariOccupati() {
        return repo.findAll().stream()
                .map(a -> a.getDataOra().toString())  
                .toList();
    }
    
}
