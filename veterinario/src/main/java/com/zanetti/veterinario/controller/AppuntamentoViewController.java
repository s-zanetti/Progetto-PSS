package com.zanetti.veterinario.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zanetti.veterinario.model.Animale;
import com.zanetti.veterinario.model.Appuntamento;
import com.zanetti.veterinario.model.Cliente;
import com.zanetti.veterinario.repository.AnimaleRepository;
import com.zanetti.veterinario.repository.AppuntamentoRepository;
import com.zanetti.veterinario.repository.ClienteRepository;

import jakarta.servlet.http.HttpSession;

 
@Controller
@RequestMapping("/")
public class AppuntamentoViewController {

    @Autowired
    private AppuntamentoRepository repo;

    @Autowired
    private AnimaleRepository animaleRepo;

    @Autowired
    private ClienteRepository clienteRepo;
    
    // Mostra la pagina appuntamenti-admin.html con tutti gli appuntamenti visibili all'admin
    @GetMapping("/admin")
    public String paginaAppuntamentiAdmin(Model model, HttpSession session) {
        String admin = (String) session.getAttribute("admin");
        if (admin == null || !admin.equals("true")) {
            return "redirect:/login";
        }
        model.addAttribute("appuntamenti", repo.findAllByOrderByDataOraAsc());
        model.addAttribute("clienti", clienteRepo.findAll());
        

        return "appuntamenti-admin";
    }

    // Aggiunta appuntamento lato admin
    @PostMapping("/admin/aggiungi")
    public String aggiungiAppuntamentoAdmin(@RequestParam Long animaleId,
                                            @RequestParam String data,
                                            @RequestParam String ora,
                                            @RequestParam String motivo,
                                            HttpSession session,
                                            Model model) {

        String admin = (String) session.getAttribute("admin");
        if (admin == null || !admin.equals("true")) {
            return "redirect:/login";
        }

        Animale animale = animaleRepo.findById(animaleId).orElse(null);

        if (animale != null) {
            LocalDateTime dataOra = LocalDateTime.parse(data + "T" + ora);
            
            if (dataOra.isBefore(LocalDateTime.now())) {
                model.addAttribute("erroreData", "Non puoi prenotare una data nel passato.");
                model.addAttribute("appuntamenti", repo.findAllByOrderByDataOraAsc());
                model.addAttribute("clienti", clienteRepo.findAll());
                return "appuntamenti-admin";
            }
            
            Appuntamento nuovo = new Appuntamento();
            nuovo.setDataOra(dataOra);
            nuovo.setMotivo(motivo);
            nuovo.setAnimale(animale);
            repo.save(nuovo);
        }

        return "redirect:/admin";
    }

    // Pagina per prenotare un appuntamento lato cliente
    @GetMapping("/area-clienti/prenota")
    public String paginaPrenotazione(HttpSession session, Model model) {
        Long clienteId = (Long) session.getAttribute("clienteId");
        if (clienteId == null) {
            return "redirect:/login";
        }

        Cliente cliente = clienteRepo.findById(clienteId).orElse(null);
        if (cliente == null) {
            return "redirect:/login";
        }

        System.out.println("Accesso prenotazione - Cliente: " + cliente.getId());
        model.addAttribute("animali", cliente.getAnimali());
              
        
        return "prenota-appuntamento";
    }
    
    
    // prenotare lato cliente
    
    @PostMapping("/area-clienti/prenota")
    public String salvaPrenotazioneCliente(@RequestParam String data,
                                           @RequestParam String ora,
                                           @RequestParam String motivo,
                                           @RequestParam Long animaleId,
                                           HttpSession session,
                                           Model model) {

        Long clienteId = (Long) session.getAttribute("clienteId");
        if (clienteId == null) {
            return "redirect:/login";
        }

        Animale animale = animaleRepo.findById(animaleId).orElse(null);

        if (animale == null || !animale.getProprietario().getId().equals(clienteId)) {
            return "redirect:/area-clienti?errore=accesso";
        }

        LocalDateTime dataOra = LocalDateTime.parse(data + "T" + ora);
        if (dataOra.isBefore(LocalDateTime.now())) {
            return "redirect:/area-clienti/prenota?erroreData";
        }
        
        boolean esisteGia = repo.existsByDataOra(dataOra);
        if (esisteGia) {
            model.addAttribute("erroreOrario", "La fascia oraria selezionata è già occupata.");
            
            // Ricarica gli animali per il form
            Cliente cliente = clienteRepo.findById(clienteId).orElse(null);
            model.addAttribute("animali", cliente.getAnimali());

            return "prenota-appuntamento";
        }

        

        Appuntamento nuovo = new Appuntamento();
        nuovo.setDataOra(dataOra);
        nuovo.setMotivo(motivo);
        nuovo.setAnimale(animale);
        repo.save(nuovo);

        return "redirect:/area-clienti";
    }

    
    
    //Elimina appuntamento lato admin
    @PostMapping("/admin/elimina")
    public String eliminaAppuntamentoAdmin(@RequestParam Long id, HttpSession session) {
        
        String admin = (String) session.getAttribute("admin");
        if (admin == null || !admin.equals("true")) {
            return "redirect:/login";
        }

        repo.deleteById(id);

        return "redirect:/admin";
    }


    // Eliminazione Appuntamento lato cliente
 
    @PostMapping("/elimina")
    public String eliminaAppuntamento(@RequestParam Long id, HttpSession session) {

        Long clienteId = (Long) session.getAttribute("clienteId");
        if (clienteId == null) {
            return "redirect:/login";
        }

        Appuntamento app = repo.findById(id).orElse(null);

        if (app != null && app.getAnimale().getProprietario().getId().equals(clienteId)) {
            
            if (app.getDataOra().isAfter(LocalDateTime.now().plusHours(24))) {
                repo.deleteById(id);
            }
        }

        return "redirect:/area-clienti";
    }
   


    // Restituisce gli animali di un cliente in JSON
    @GetMapping("/admin/animali/{clienteId}")
    @ResponseBody
    public List<Animale> getAnimaliPerCliente(@PathVariable Long clienteId) {
        return animaleRepo.findByProprietarioId(clienteId);
    } 
    

    
}

