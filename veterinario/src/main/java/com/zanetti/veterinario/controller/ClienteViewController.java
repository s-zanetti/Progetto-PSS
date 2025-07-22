package com.zanetti.veterinario.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zanetti.veterinario.model.Cliente;

import com.zanetti.veterinario.repository.ClienteRepository;

import jakarta.servlet.http.HttpSession;



@Controller
@RequestMapping("/clienti")
public class ClienteViewController {
	
	

    @Autowired
    private ClienteRepository repo;
    
   
    // mostra pagina clienti.html
    @GetMapping("/admin")
    public String paginaClienti(Model model) {
        model.addAttribute("clienti", repo.findAll());
        return "clienti"; 
    }
    
    // Aggiunge cliente
    @PostMapping("/aggiungi")
    public String aggiungiCliente(@RequestParam String nome,
    							  @RequestParam String cognome,
                                  @RequestParam String email,
                                  @RequestParam String password,
                                  @RequestParam String telefono,
                                  RedirectAttributes redirectAttributes) {
    	if (repo.existsByEmail(email)) {
            redirectAttributes.addFlashAttribute("erroreEmail", "Email già registrata, riprova.");
            return "redirect:/clienti/admin";
        }
    	
    	
        Cliente nuovo = new Cliente();
        nuovo.setNome(nome);
        nuovo.setCognome(cognome);
        nuovo.setEmail(email);
        nuovo.setPassword(password);
        nuovo.setTelefono(telefono);
        repo.save(nuovo);
        return "redirect:/clienti/admin"; // Torna alla lista clienti
    }
    
    // Elimina un cliente
    @PostMapping("/{id}/elimina")
    public String eliminaCliente(@PathVariable Long id) {
    	System.out.println(id);
        repo.deleteById(id);
        return "redirect:/clienti/admin";
    }
    
    // Modifica Cliente lato cliente
    @PostMapping("/modifica")
    public String modificaCliente(@RequestParam String nome,
                                   @RequestParam String cognome,
                                   @RequestParam String email,
                                   @RequestParam String telefono,
                                   @RequestParam String password,
                                   HttpSession session) {

        Long clienteId = (Long) session.getAttribute("clienteId");

        if (clienteId == null) {
            return "redirect:/login";
        }

        Cliente cliente = repo.findById(clienteId).orElse(null);

        if (cliente != null) {
            cliente.setNome(nome);
            cliente.setCognome(cognome);
            cliente.setEmail(email);
            cliente.setTelefono(telefono);
            cliente.setPassword(password);  
            repo.save(cliente);
        }

        return "redirect:/area-clienti";
    }

    
}


    
