package com.zanetti.veterinario.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zanetti.veterinario.model.Animale;
import com.zanetti.veterinario.model.Cliente;
import com.zanetti.veterinario.repository.AnimaleRepository;
import com.zanetti.veterinario.repository.ClienteRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/animali")
public class AnimaleViewController {

    @Autowired
    private AnimaleRepository repo;

    @Autowired
    private ClienteRepository clienteRepo;
    
    // Accesso admin
    @GetMapping("/admin")
    public String paginaAnimaliAdmin(Model model, HttpSession session) {
        String admin = (String) session.getAttribute("admin");
        if (admin == null || !admin.equals("true")) {
            return "redirect:/login";
        }
        
        model.addAttribute("animali", repo.findAll());
        model.addAttribute("clienti", clienteRepo.findAll());
        return "animali";
    }


 // Aggiunge un nuovo animale associato a un cliente
    @PostMapping("/aggiungi")
    public String aggiungiAnimale(@RequestParam String nome,
                                  @RequestParam String specie,
                                  @RequestParam String razza,
                                  @RequestParam String sesso,
                                  @RequestParam String data,
                                  @RequestParam Long proprietarioId,
                                  RedirectAttributes redirectAttributes) {

        Cliente proprietario = clienteRepo.findById(proprietarioId).orElse(null);

        if (proprietario != null) {
        	
        	LocalDate dataNascita = LocalDate.parse(data);
        	 if (dataNascita.isAfter(LocalDate.now())) {
                 redirectAttributes.addFlashAttribute("erroreData", "La data di nascita non può essere nel futuro.");
                 return "redirect:/animali/admin";
             }

            Animale nuovo = new Animale();
            nuovo.setNome(nome);
            nuovo.setSpecie(specie);
            nuovo.setRazza(razza);
            nuovo.setSesso(sesso);
            nuovo.setDataNascita(LocalDate.parse(data));
            nuovo.setProprietario(proprietario);

            repo.save(nuovo);
        }

        return "redirect:/animali/admin";
    }
    
    
    // pagina
    @GetMapping("/area-clienti/aggiungi")
    public String mostraAggiungiAnimale(HttpSession session) {
        if (session.getAttribute("clienteId") == null) {
            return "redirect:/login";
        }
        return "aggiungi-animale"; 
    }

    
    //Il cliente aggiunge un animale
    @PostMapping("/area-clienti/aggiungi")
    public String aggiungiAnimaleCliente(@RequestParam String nome,
                                         @RequestParam String specie,
                                         @RequestParam String razza,
                                         @RequestParam String sesso,
                                         @RequestParam String data,
                                         HttpSession session,
                                         RedirectAttributes redirectAttributes) {

        Long clienteId = (Long) session.getAttribute("clienteId");
        if (clienteId == null) {
            return "redirect:/login";
        }

        Cliente proprietario = clienteRepo.findById(clienteId).orElse(null);
        
        LocalDate dataNascita = LocalDate.parse(data);
   	 if (dataNascita.isAfter(LocalDate.now())) {
            redirectAttributes.addFlashAttribute("erroreData", "La data di nascita non può essere nel futuro.");
            return "redirect:/animali/admin";
        }

        if (proprietario != null) {
            Animale nuovo = new Animale();
            nuovo.setNome(nome);
            nuovo.setSpecie(specie);
            nuovo.setRazza(razza);
            nuovo.setSesso(sesso);
            nuovo.setDataNascita(LocalDate.parse(data));
            nuovo.setProprietario(proprietario);

            repo.save(nuovo);
        }

        return "redirect:/area-clienti"; 
    }



    // Elimina un animale
    @PostMapping("/admin/elimina")
    public String eliminaAnimaleAdmin(@RequestParam Long id, HttpSession session) {
        String admin = (String) session.getAttribute("admin");
        if (admin == null || !admin.equals("true")) {
            return "redirect:/login";
        }

        if (repo.existsById(id)) {
            repo.deleteById(id);
            repo.flush(); 
            System.out.println("Animale eliminato con ID: " + id);
        } else {
            System.out.println("Animale NON trovato nel DB");
        }

        return "redirect:/animali/admin";
    }

    
}
