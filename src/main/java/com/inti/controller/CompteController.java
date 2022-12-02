package com.inti.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inti.model.Compte;
import com.inti.service.ICompteRepository;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/compte")
@Slf4j
public class CompteController {

	@Autowired
	ICompteRepository icr;

	@Autowired
	JmsTemplate jmsTemplate;
	
	
	
	@PostMapping("/creerCompte")
	public boolean creerCompte(@RequestBody Compte cp) {
		if (cp.getNumCompte() > 0) {
			log.info("Le compte a bien été creer");
			icr.save(cp);
			return true;
		}
		log.error("Veuillez saisir un numero de compte valide");
		return false;
	}

	@GetMapping("/listecompte")
	public List<Compte> getAllCompte() {
		log.info("Liste de tous les comptes");
		return icr.findAll();
	}

	@GetMapping("/getCompte/{numCompte}")
	public Compte compteExisant(@PathVariable long numCompte) {
		try {
			log.info("Le compte a bien été afficher");
			return icr.findById(numCompte).get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.error("Veuillez saisir un numero de compte valide");
		return null;
	}

	@DeleteMapping("/deleteCompte/{numCompte}")
	public boolean deleteCompte(@PathVariable long numCompte) {
		if (numCompte != 0) {
			log.info("Le compte a bien été supprimer");
			icr.deleteById(numCompte);
			return true;
		}
		log.error("Veuillez saisir un numero de compte valide");
		return false;
	}

	@PutMapping("/updateCompte/{numCompte}")
	public Compte updateCompte(@RequestBody Compte nouveauCompte, @PathVariable long numCompte) {
		return icr.findById(numCompte).map(Compte -> {
			Compte.setNumCompte(nouveauCompte.getNumCompte());
			Compte.setNomProprio(nouveauCompte.getNomProprio());
			Compte.setPlafondDepot(nouveauCompte.getPlafondDepot());
			Compte.setPlafondRetrait(nouveauCompte.getPlafondRetrait());
			Compte.setSolde(nouveauCompte.getSolde());
			Compte.setAggio(nouveauCompte.getAggio());
			Compte.setDecouvertMax(nouveauCompte.getDecouvertMax());
			return icr.save(Compte);
		}).orElseGet(() -> {
			return icr.save(nouveauCompte);
		});

	}
	@GetMapping("/retraitposs/{numCompte}")
	 public boolean retraitPossible(@PathVariable long numCompte, @PathVariable double montant) {
			Compte compte = icr.findById(numCompte).get();
			if (compte.getDecouvertMax() < (compte.getSolde() - montant) && compte.getPlafondRetrait() > montant) {
				return true;
				}else if (compte.getPlafondRetrait() < montant) {
					System.out.println("le montant max du retrait a été depassé");
					return false;
				} else {
					System.out.println("le montant max du decouvert a été depassé");
					return false;
				}
		
		
}
	
	@GetMapping("depot/{numCompte}/{montant}")
	public boolean depotPossible(@PathVariable long numCompte, @PathVariable double montant) {
		Compte compte = icr.findById(numCompte).get();
		if (compte.getPlafondDepot() > montant) {
			System.out.println("le compte " + compte.getNumCompte() + " a été augmenté de " + montant + "pour un total de " + compte.getSolde());
			return true;
		} else {
			System.out.println("le montant max du depot a été depassé");
			return false;
		}
	}
	
	public void sendName(String banque,String name) {
		jmsTemplate.convertAndSend(banque, name);
			}

		
		
}
