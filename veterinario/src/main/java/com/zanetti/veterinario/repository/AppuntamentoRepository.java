package com.zanetti.veterinario.repository;

import com.zanetti.veterinario.model.Appuntamento;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppuntamentoRepository extends JpaRepository<Appuntamento, Long> {
	
	// Verifica se esiste già un appuntamento in una data/ora
	boolean existsByDataOra(LocalDateTime dataOra);
	
	// Trova appuntamenti di un cliente in data crescente
	List<Appuntamento> findByAnimale_Proprietario_Id(Long clienteId);
	List<Appuntamento> findAllByOrderByDataOraAsc();
	
	// Cancella appuntamenti vecchi
	void deleteByDataOraBefore(LocalDateTime dataOra);

}

