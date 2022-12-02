package com.inti.operation;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inti.model.Compte;

import lombok.extern.slf4j.Slf4j;
@RestController
@RequestMapping("Banque")
@Slf4j
public class OperationSimpleController {
	@PutMapping("retrait/{id}")
	public boolean retrait(@RequestBody Compte compte,@PathVariable double montant) {
		if (compte.getDecouvertMax() < (compte.getSolde()- montant) && compte.getPlafondRetrait() > montant) {
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
	@PutMapping("depot/{id}")
	public boolean depot(@RequestBody Compte compte,@PathVariable double montant) {
		if (compte.getPlafondDepot() > montant) {
			compte.setSolde(compte.getSolde() + montant);
			System.out.println("le compte " + compte.getNumCompte() + " a été augmenté de " + montant);
			return true;
		} else {
			System.out.println("le montant max du depot a été depassé");
			return false;
		}
	}
		
}
	
