package com.zanetti.veterinario.controller;

import com.zanetti.veterinario.model.Animale;
import com.zanetti.veterinario.model.Cliente;
import com.zanetti.veterinario.repository.AnimaleRepository;
import com.zanetti.veterinario.repository.AppuntamentoRepository;
import com.zanetti.veterinario.repository.ClienteRepository;

import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    @Autowired
    private ClienteRepository clienteRepo;
    @Autowired
    private AnimaleRepository animaleRepo;
    @Autowired
    private AppuntamentoRepository appuntamentoRepo;

    private final String adminUser = "admin";
    private final String adminPass = "admin";
    

    //Registra cliente e relativo animale
    @PostMapping("/registrati")
    public String registraCliente(@RequestParam String nome, 
                                   @RequestParam String cognome,
                                   @RequestParam String email,
                                   @RequestParam String telefono,
                                   @RequestParam String password,
                                   @RequestParam String confermaPassword,
                                   @RequestParam String nomeAnimale,
                                   @RequestParam String specieAnimale,
                                   @RequestParam String razzaAnimale,
                                   @RequestParam String sessoAnimale,
                                   @RequestParam String dataNascitaAnimale,
                                   RedirectAttributes redirectAttributes,
                                   HttpSession session) {
    	
    	if (clienteRepo.existsByEmail(email)) {
            redirectAttributes.addFlashAttribute("erroreEmail", "Email già registrata, riprova.");
            return "redirect:/";
        }
    	
    	if (!password.equals(confermaPassword)) {
    	    redirectAttributes.addFlashAttribute("errorePassword", "Le password non coincidono.");
    	    return "redirect:/";
    	}
    
    	
        Cliente nuovo = new Cliente();
        nuovo.setNome(nome);
        nuovo.setCognome(cognome);
        nuovo.setEmail(email);
        nuovo.setTelefono(telefono);
        nuovo.setPassword(password);
        clienteRepo.save(nuovo);     
            
        Animale animale = new Animale();
        animale.setNome(nomeAnimale);
        animale.setSpecie(specieAnimale);
        animale.setRazza(razzaAnimale);
        animale.setSesso(sessoAnimale);
        animale.setDataNascita(LocalDate.parse(dataNascitaAnimale));
        animale.setProprietario(nuovo);
        animaleRepo.save(animale);

        session.setAttribute("clienteId", nuovo.getId());

        return "redirect:/area-clienti";
    }

    
    // Mostra login
    @GetMapping("/login")
    public String paginaLogin() {
        return "login";
    }
    
   


     // Verifica credenziali
    @PostMapping("/login")
    public String verificaLogin(@RequestParam String username,
                                @RequestParam String password,
                                HttpSession session,
                                Model model) {

        // Caso Admin
        if (username.equals(adminUser) && password.equals(adminPass)) {
        	session.setAttribute("admin", "true");
            return "redirect:/home-admin";
        }

        // Caso Cliente - Cerca nel DB
        Cliente cliente = clienteRepo.findAll().stream()
                .filter(c -> c.getEmail().equals(username) && c.getPassword().equals(password))
                .findFirst()
                .orElse(null);

        if (cliente != null) {
            session.setAttribute("clienteId", cliente.getId());
            return "redirect:/area-clienti";
        }

        // Credenziali non valide
        model.addAttribute("errore", true);
        return "login";
    } 
    
    // Mostra la home admin
    @GetMapping("/home-admin")
    public String homeAdmin() {
        return "home-admin";
    }

    
    // Mostra area clienti
    @GetMapping("/area-clienti")
    public String areaCliente(HttpSession session, Model model) {
        Long clienteId = (Long) session.getAttribute("clienteId");
        if (clienteId == null) {
            return "redirect:/login";
        }

        Cliente cliente = clienteRepo.findById(clienteId).orElse(null);
        if (cliente == null) {
            return "redirect:/login";
        }

        model.addAttribute("cliente", cliente);
        model.addAttribute("animali", cliente.getAnimali());
        model.addAttribute("appuntamenti", appuntamentoRepo.findByAnimale_Proprietario_Id(clienteId));

        return "area-clienti";
    }

    
    // Torna alla home invalidando la sessione
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
    
}