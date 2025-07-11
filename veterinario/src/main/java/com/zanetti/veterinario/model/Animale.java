package com.zanetti.veterinario.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity
public class Animale {

	// Dati Animale
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Il nome è obbligatorio")
    private String nome;
    
    @NotBlank(message = "Inserire la specie")
    private String specie;
    
    @NotBlank(message = "Inserire la razza")
    private String razza;
    
    @NotBlank(message = "inserire il sesso")
    private String sesso;
    
    @NotNull(message = "inserire la data di nascita")
    private LocalDate dataNascita;
    

    @ManyToOne
    @JoinColumn(name = "proprietario_id")
    @JsonBackReference
    private Cliente proprietario;
    
    @OneToMany(mappedBy = "animale", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Appuntamento> appuntamenti;


    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getSpecie() { return specie; }
    public void setSpecie(String specie) { this.specie = specie; }

    public Cliente getProprietario() { return proprietario; }
    public void setProprietario(Cliente proprietario) { this.proprietario = proprietario; }

    public String getRazza() { return razza; }
    public void setRazza(String razza) { this.razza = razza; }

    public String getSesso() { return sesso; }
    public void setSesso(String sesso) { this.sesso = sesso; }
    
    public LocalDate getDataNascita() { return dataNascita; }
    public void setDataNascita(LocalDate dataNascita) { this.dataNascita = dataNascita; }

    public List<Appuntamento> getAppuntamenti() { return appuntamenti;}
    public void setAppuntamenti(List<Appuntamento> appuntamenti) { this.appuntamenti = appuntamenti;
    }


}


