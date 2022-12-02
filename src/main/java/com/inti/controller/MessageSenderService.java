package com.inti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.inti.model.Compte;
import com.inti.service.IBanqueRepository;
import com.inti.service.ICompteRepository;

@RestController
@Component
public class MessageSenderService {
	
	private static final String MESSAGE_QUEUE = "PROJETJMS";
	
	@Autowired
	JmsTemplate jmsTemplate;
	
	@Autowired
	ICompteRepository icr;
	
	@Autowired
	IBanqueRepository ibr;
	
	@GetMapping("/virementJMS/{numCompteEnvoie}/{numCompteDestinataire}/{montant}")
	public String verifPlafond (@PathVariable long numCompteEnvoie, @PathVariable long numCompteDestinataire,  @PathVariable double montant) 
	{
		Compte compte = icr.findById(numCompteEnvoie).get();
		
		String verif = Boolean.toString(compte.autoriseRetrait(montant)) + "/" +  Long.toString(numCompteEnvoie) + "/" + Long.toString(numCompteDestinataire) + "/" + Double.toString(montant) ;
//		jmsTemplate.setDefaultDestinationName(MESSAGE_QUEUE);
		jmsTemplate.convertAndSend(MESSAGE_QUEUE, verif);
		return verif;
	}
	
	
	


}
