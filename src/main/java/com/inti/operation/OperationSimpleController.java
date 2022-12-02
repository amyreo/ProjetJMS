package com.inti.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inti.model.Compte;
import com.inti.service.ICompteRepository;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("Banque")
@Slf4j
public class OperationSimpleController {

	@Autowired
	ICompteRepository icr;

	@PutMapping("retrait/{montant}")
	public boolean retrait(@RequestBody Compte compte, @PathVariable double montant) {
		if (compte.getDecouvertMax() < (compte.getSolde() - montant) && compte.getPlafondRetrait() > montant) {
			compte.setSolde(compte.getSolde() - montant);
			System.out.println("le compte " + compte.getNumCompte() + " a ete debité de " + montant);
			return true;
		} else if (compte.getPlafondRetrait() < montant) {
			System.out.println("le montant max du retrait a été depassé");
			return false;
		} else {
			System.out.println("le montant max du decouvert a été depassé");
			return false;
		}
	}

	@PutMapping("depot/{montant}")
	public boolean depot(@RequestBody Compte compte, @PathVariable double montant) {
		if (compte.getPlafondDepot() > montant) {
			compte.setSolde(compte.getSolde() + montant);
			System.out.println("le compte " + compte.getNumCompte() + " a été augmenté de " + montant);
			return true;
		} else {
			System.out.println("le montant max du depot a été depassé");
			return false;
		}
	}

	@GetMapping("/virement/{idEnvoyeur}/{idDestinataire}/{montant}")
	public String virement(@PathVariable long idEnvoyeur, @PathVariable long idDestinataire,
			@PathVariable double montant) {
		Compte envoyeur = icr.findById(idEnvoyeur).get();
		Compte destinataire = icr.findById(idDestinataire).get();
		if (envoyeur.autoriseRetrait(montant) || destinataire.autoriseDepot(montant)) {
			retrait(envoyeur, montant);
			depot(destinataire, montant);
			return "Le virement a bien été effectué";
		} else {
			return "Une erreur est survenue pendant le virement";
		}

	}

}
