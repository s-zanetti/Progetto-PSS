package com.zanetti.veterinario.repository;

import com.zanetti.veterinario.model.Animale;


import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimaleRepository extends JpaRepository<Animale, Long> {
	 
	// Animali associati a un cliente specifico
	List<Animale> findByProprietarioId(Long proprietarioId);
	 
	@EntityGraph(attributePaths = {"proprietario"})
	List<Animale> findAll();
	 
	 
}
