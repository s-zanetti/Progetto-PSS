package com.zanetti.veterinario.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.zanetti.veterinario.repository.AppuntamentoRepository;

import java.time.LocalDateTime;

@Component
public class PuliziaAppuntamentiService {

    private final AppuntamentoRepository repo;

    public PuliziaAppuntamentiService(AppuntamentoRepository repo) {
        this.repo = repo;
    }

    @Scheduled(cron = "0 * * * * *") // Esegue ogni minuto
    @Transactional
    public void eliminaAppuntamentiScaduti() {
        LocalDateTime oraAttuale = LocalDateTime.now();
        repo.deleteByDataOraBefore(oraAttuale);
    }
}
