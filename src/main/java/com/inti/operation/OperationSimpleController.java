package com.inti.operation;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inti.JMS.JmsController;
import com.inti.controller.CompteController;
import com.inti.model.Compte;
import com.inti.service.ICompteRepository;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("Banque")
@Slf4j
public class OperationSimpleController {

	String messageRecu = "V" + "100000" + " IdCompteE: " + "234567" + " IdCompteD: " + "123456";

	JmsTemplate jmsTemplate;

	CompteController compteController;

	@Autowired
	ICompteRepository icr;

	@PutMapping("retrait/{numCompte}/{montant}")
	public boolean retrait(@PathVariable long numCompte, @PathVariable double montant) {
		Compte compte = icr.findById(numCompte).get();
		if (compteController.retraitPossible(numCompte, montant) == true) {
			compte.setSolde(compte.getSolde() - montant);
			icr.save(compte);
			System.out.println("le compte " + compte.getNumCompte() + " a ete debité de " + montant
					+ "pour un total de " + compte.getSolde());
			return true;
		} else {
			return false;
		}
	}

	@PutMapping("depot/{numCompte}/{montant}")
	public boolean depot(@PathVariable long numCompte, @PathVariable double montant) {
		Compte compte = icr.findById(numCompte).get();
		if (compteController.depotPossible(numCompte, montant) == true) {
			compte.setSolde(compte.getSolde() + montant);
			icr.save(compte);
			System.out.println("le compte " + compte.getNumCompte() + " a été augmenté de " + montant
					+ "pour un total de " + compte.getSolde());
			return true;
		} else {
			return false;
		}
	}

	@PutMapping("/virement/{idEnvoyeur}/{idDestinataire}/{montant}")
	public String virement(@PathVariable long idEnvoyeur, @PathVariable long idDestinataire,
			@PathVariable double montant) {
		Compte envoyeur = icr.findById(idEnvoyeur).get();
		Compte destinataire = icr.findById(idDestinataire).get();
		if (envoyeur.getBanque() == destinataire.getBanque()) {

			if (envoyeur.autoriseRetrait(montant) && destinataire.autoriseDepot(montant)) {
				retrait(idEnvoyeur, montant);
				depot(idDestinataire, montant);
				return "Le virement a bien été effectué";
			} else {
				return "Une erreur est survenue pendant le virement";
			}
		} else {
			if ((envoyeur.autoriseRetrait(montant))
					&& (destinataire.getBanque().nomBanque == "FE" || destinataire.getBanque().nomBanque == "JL"
							|| destinataire.getBanque().nomBanque == "TL"
							|| destinataire.getBanque().nomBanque == "FD")) {
				String textE = "V: " + montant + " IdCompteE: " + envoyeur.getNumCompte() + " IdCompteD: "
						+ destinataire.getNumCompte();
				compteController.sendName(destinataire.getBanque().getNomBanque(), textE);
				System.out.println("la requete a ete envoyé veuiller patienté");
				return "la requete a ete envoyé veuiller patienté";
			} else {
				System.out.println("banque introuvable CHEH");
				return "Une erreur est survenue pendant le virement";
			}
		}
	}

	public String virementRequis(String messageRecu) {

		String mots[] = messageRecu.split(" ");
		if (mots[0] == "V" && mots[3] == "IdCompteE" && mots[5] == "IdCompteD") {
			long montant = Long.parseLong(mots[2]);
			long idDestinataire = Long.parseLong(mots[6]);
			long idEnvoyeur = Long.parseLong(mots[4]);
			Compte envoyeur = icr.findById(idEnvoyeur).get();
			Compte destinataire = icr.findById(idDestinataire).get();

			if (destinataire.autoriseDepot(montant)) {
				retrait(idEnvoyeur, montant);
				depot(idDestinataire, montant);
				String textD ="its ok";
				compteController.sendName(envoyeur.getBanque().getNomBanque(), textD);
				return "Le virement a bien été effectué";
			} 
		}
		return "Une erreur est survenue pendant le virement";
	}
}
