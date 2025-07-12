package com.zanetti.veterinario.repository;

import com.zanetti.veterinario.model.Cliente;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
	
	// Ricerca cliente per email
	Optional<Cliente> findByEmail(String email);
	
	boolean existsByEmail(String email);
}