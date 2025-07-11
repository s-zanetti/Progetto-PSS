package com.zanetti.veterinario.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Appuntamento {

	// Informazioni dell'appuntamento
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataOra;
    private String motivo;

    @ManyToOne
    @JoinColumn(name = "animale_id")
    @JsonBackReference
    private Animale animale;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getDataOra() { return dataOra; }
    public void setDataOra(LocalDateTime dataOra) { this.dataOra = dataOra; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public Animale getAnimale() { return animale; }
    public void setAnimale(Animale animale) { this.animale = animale; }
}

