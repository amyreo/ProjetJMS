package com.inti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inti.model.Compte;
import com.inti.service.ICompteRepository;

@RestController
@RequestMapping("ouvrircompte")
public class FonctionOuvrirCompte {

	@Autowired
	ICompteRepository icr;
	
	@PostMapping("save")
	public boolean ouvrirCompte(@RequestBody Compte compte) {
		
		if (compte != null) {
			icr.save(compte);
			return true;
		}
		return false;
	}
}
